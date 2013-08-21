package com.rauma.lille.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.AttachmentType;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.rauma.lille.Resource;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.Utils;
import com.rauma.lille.armory.BulletFactory;
import com.rauma.lille.network.ApplyDamageCommand;
import com.rauma.lille.network.Command;
import com.rauma.lille.network.KillCommand;

/**
 * @author frank
 *
 */
public class SimplePlayer extends BodyImageActor {
	private static final float FIRE_RATE = 0.1f;
	private float lastFired;
	private BulletFactory bulletFactory;
	private int successfulShot = 0;
	private int failedShot = 0;
	private float health = 100;
	private int playerId = -1;
	private float angleRad;
	private SpaceGame game;
	private boolean me;
	private short categoryBits;
	private short maskBits;
	private boolean isStaticBody;
	private World world;
	
	private TextureAtlas atlas; // The information used to find the correct texture
	private SkeletonData skeletonData; //The json file from Spine
	private Skeleton skeleton; // The skeleton containing the json data (skeletonData) from Spine
	private Animation animation; //The object used to store an animation found in the SkeletonData and to be applied later
	private SkeletonRenderer skeletonRenderer = new SkeletonRenderer(); // The object used to draw the skeleton
	private Box2dAttachment bodyAttachment;
	private float time;
	private int lastHitByPlayerId = -1;
	private boolean isDead = false;
	
	public SimplePlayer(int playerId, String name, short categoryBits, short maskBits, float x, float y, World world, BulletFactory bulletFactory, boolean isStaticBody, SpaceGame game, boolean me) {
		super(new TextureRegion(Resource.emptyTexture, 0, 0, 64, 64));
		this.categoryBits = categoryBits;
		this.maskBits = maskBits;
		this.world = world;
		this.bulletFactory = bulletFactory;
		this.playerId = playerId;
		this.isStaticBody = isStaticBody;
		this.game = game;
		this.me = me;
		
		atlas = new TextureAtlas(Gdx.files.internal("mech.atlas"));
		// This loader creates Box2dAttachments instead of RegionAttachments for an easy way to keep
		// track of the Box2D body for each attachment.
		AtlasAttachmentLoader atlasLoader = new AtlasAttachmentLoader(atlas) {
			public Attachment newAttachment (Skin skin, AttachmentType type, String name) {
				Box2dAttachment attachment = new Box2dAttachment(name);
				AtlasRegion region = atlas.findRegion(attachment.getName());
				if (region == null) throw new RuntimeException("Region not found in atlas: " + attachment);
				attachment.setRegion(region);
				return attachment;
			}
		};
		SkeletonJson json = new SkeletonJson(atlasLoader);
		json.setScale(0.10f);
		skeletonData = json.readSkeletonData(Gdx.files.internal("mech.json"));
		animation = skeletonData.findAnimation("idle");
		skeleton = new Skeleton(skeletonData);
		skeleton.setX(x);
		skeleton.setY(y);
		skeleton.updateWorldTransform();
		
		
		PolygonShape boxPoly = new PolygonShape();
		
		// body attachment
		bodyAttachment = (Box2dAttachment)skeleton.getAttachment("body","body");
		createShapeOnAttachment(bodyAttachment,boxPoly);
		// player
		float width = bodyAttachment.getWidth();
		float height = bodyAttachment.getHeight();

		BodyType type;
		if(isStaticBody){
			type = BodyType.StaticBody;
		}else{
			type = BodyType.DynamicBody;
		}
		createBodyDefOnAttachment(bodyAttachment, type, boxPoly);
		
		setName(name);
		setOrigin(width / 2, height / 2);
		setWidth(width);
		setHeight(height);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if(health < 1 && !isDead  ) {
			die();
		}
		lastFired += delta;
	}

	public boolean fireWeapon(float angleRad) {
		// player.getBody().setTransform(player.getBody().getPosition(),
		// angleRad);
		boolean shotFired = false;
		if (lastFired >= FIRE_RATE) {
			Bullet bullet = bulletFactory.getBullet();
			if (bullet != null) {
				getStage().addActor(bullet);
				float offsetX = getWidth()+5;
				float offsetY = 12;
				if(MathUtils.sin(angleRad) < 0){
					offsetX = -10;
					if(!skeleton.getFlipX()){
						skeleton.setFlipX(true);
					}
				}else{
					if(skeleton.getFlipX()){
						skeleton.setFlipX(false);
					}
				}
				bullet.fire(getX() + offsetX, getY()+getHeight()/2 + offsetY, angleRad);
				lastFired = 0f;
				successfulShot++;
				shotFired = true;
			} else {
				System.out.println("Out of ammo / Weapon needs cool down");
				failedShot++;
			}
		}
		return shotFired;
	}

	public void applyDamage(float damage, int bulletFiredByPlayerId) {
		this.health -= damage;
		System.out.println("New health: " + health);
		lastHitByPlayerId  = bulletFiredByPlayerId;
	}
	
	private void die() {
		if(isMe()){
			isDead = true;
			Command c = new KillCommand(playerId, lastHitByPlayerId);
			game.writeToServer(c);
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
//		batch.end(); // actual drawing is done on end(); if we do not end, we contaminate previous rendering.
//		batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
//		batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//		batch.begin();
		Array<Slot> slots = skeleton.getSlots();
		for(Slot s : slots){
			Color newColor = s.getColor();
			newColor.a = health/100;
		}
		//TODO (john) set transparency to the attachment on the skeleton maybe?
		float delta = Gdx.graphics.getDeltaTime();
		float remaining = delta;
		float lastTime = time;
		while (remaining > 0) {
			float d = Math.min(1/60f, remaining);
			time += d;
			remaining -= d;
		}
		skeleton.setX(bodyAttachment.getWorldX()*SpaceGame.WORLD_SCALE);
		skeleton.setY(bodyAttachment.getWorldY()*SpaceGame.WORLD_SCALE-bodyAttachment.getY()-bodyAttachment.getHeight()*bodyAttachment.getScaleY()/2);
		Bone bodyBone = skeleton.findBone("body");
		Array<Event> events = new Array<Event>();
		animation.apply(skeleton, lastTime, time, true, events);
		for(Event e : events){
			String name = e.getString();
			System.out.println(e);
			System.out.println(name);
		}
		bodyBone.setRotation(bodyBone.getRotation());
		skeleton.updateWorldTransform();
		skeletonRenderer.draw(batch, skeleton);
		super.draw(batch, parentAlpha);
	}

	public int getId() {
		return playerId;
	}

	public void setAngleRad(float angleRad) {
		this.angleRad = angleRad;
	}
	public float getAngleRad(){
		return angleRad;
	}

	public void registerHit(int firedByPlayerId, float damage) {
		Command c = new ApplyDamageCommand(playerId, damage, firedByPlayerId);
		game.writeToServer(c);
	}

	public boolean isMe() {
		return me;
	}

	public short getCategoryBits() {
		return categoryBits;
	}

	public short getMaskBits() {
		return maskBits;
	}

	public boolean isStaticBody() {
		return isStaticBody;
	}
	
	static class Box2dAttachment extends RegionAttachment {
		Body body;
		public float getWorldX(){
			return body.getPosition().x;
		}
		public float getWorldY(){
			return body.getPosition().y;
		}
		public Box2dAttachment (String name) {
			super(name);
		}
	}
	
	private void createBodyDefOnAttachment(Box2dAttachment attachment,
			BodyType bodytype, PolygonShape boxPoly) {
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodytype;
		
		boxBodyDef.position.x = Utils.Screen2World(skeleton.getX());
		boxBodyDef.position.y = Utils.Screen2World(skeleton.getY()+attachment.getY()+attachment.getHeight()*attachment.getScaleY()/2);
		boxBodyDef.angle = 0;
		attachment.body = world.createBody(boxBodyDef);
		attachment.body.setFixedRotation(true);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = boxPoly;
		fixtureDef.density = 0.0275f*attachment.getWidth()/10;
		fixtureDef.restitution = 0.1f*attachment.getWidth()/10;
		fixtureDef.friction= 0.15f*attachment.getWidth()/10;
		fixtureDef.filter.categoryBits = categoryBits;
		fixtureDef.filter.maskBits = maskBits;
		attachment.body.createFixture(fixtureDef);
		attachment.body.setUserData(this);
//		attachment.body.setAngularDamping(10.0f);
		setBody(attachment.body);
	}
	
	private void createShapeOnAttachment(Box2dAttachment attachment,
			PolygonShape boxPoly) {
		boxPoly.setAsBox(Utils.Screen2World(attachment.getWidth() * attachment.getScaleX())/2,
				Utils.Screen2World(attachment.getHeight() * attachment.getScaleY())/2,
				Utils.Screen2World(attachment.getX()* attachment.getScaleX(), attachment.getY()*attachment.getScaleY()),
				attachment.getRotation()*MathUtils.degRad);
	}
	
	public void setAnimation(String string) {
		if(!animation.getName().equals(string)){
			animation = skeletonData.findAnimation(string);
		}
	}

	public void setHealth(int health) {
		this.health = health;
	}
}
