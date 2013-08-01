package com.rauma.lille;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.BoneData;
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

public class Player extends Actor{

	TextureAtlas atlas; // The information used to find the correct texture
	SkeletonData skeletonData; //The json file from Spine
	Skeleton skeleton; // The skeleton containing the json data (skeletonData) from Spine
	Animation animation; //The object used to store an animation found in the SkeletonData and to be applied later
	SkeletonRenderer skeletonRenderer = new SkeletonRenderer(); // The object used to draw the skeleton
	float time;
	private Box2DDebugRenderer box2dRenderer = null;
	Vector2 vector = new Vector2();
	private World world = null;
	private Box2dAttachment bodyAttachment;

	public Player(World world) {
		this.world = world;
		atlas = new TextureAtlas(Gdx.files.internal("paperman.atlas"));

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
		json.setScale(0.25f);
		skeletonData = json.readSkeletonData(Gdx.files.internal("paperman.json"));
		animation = skeletonData.findAnimation("walk");
		skeleton = new Skeleton(skeletonData);
		skeleton.setX(450);
		skeleton.setY(250);
		skeleton.updateWorldTransform();


		PolygonShape boxPoly = new PolygonShape();
		
		// body attachment
		bodyAttachment = (Box2dAttachment)skeleton.getAttachment("body","sc_body");
		createShapeOnAttachment(bodyAttachment,boxPoly);
		createBodyDefOnAttachment(bodyAttachment, BodyType.DynamicBody,boxPoly);
		// left hand
		// right hand
		
		
		boxPoly.dispose();
	}
	

	private void createBodyDefOnAttachment(Box2dAttachment attachment,
			BodyType bodytype, PolygonShape boxPoly) {
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodytype;
		System.out.println(bodyAttachment.getX());
		System.out.println(bodyAttachment.getY());
		System.out.println(bodyAttachment.getWidth()/2);
		System.out.println(bodyAttachment.getHeight()/2);
		System.out.println(bodyAttachment.getScaleY());
		System.out.println(skeleton.getX());
		System.out.println(skeleton.getY());
		
		boxBodyDef.position.x = Utils.Screen2World(skeleton.getX());
		boxBodyDef.position.y = Utils.Screen2World(skeleton.getY()+bodyAttachment.getY()+bodyAttachment.getHeight()*bodyAttachment.getScaleY()/2);
//		boxBodyDef.angle = attachment.getRotation() * MathUtils.degRad;
		attachment.body = world.createBody(boxBodyDef);

		attachment.body.setFixedRotation(true);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = boxPoly;
		fixtureDef.density = 0.0195f;
		fixtureDef.restitution = 0.1f;
		fixtureDef.friction= 0.01f;
		attachment.body.createFixture(fixtureDef);

		
	}
	
	private void createShapeOnAttachment(Box2dAttachment attachment,
			PolygonShape boxPoly) {
		boxPoly.setAsBox(Utils.Screen2World(attachment.getWidth() * attachment.getScaleX())/2,
				Utils.Screen2World(attachment.getHeight() * attachment.getScaleY())/2,
				vector.set(Utils.Screen2World(attachment.getX(), attachment.getY())),
				attachment.getRotation() * MathUtils.degRad);

	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		float delta = Gdx.graphics.getDeltaTime();
		float remaining = delta;
		while (remaining > 0) {
			float d = Math.min(0.016f, remaining);
			time += d;
			remaining -= d;
		}
		skeleton.setX(bodyAttachment.getWorldX()*SpaceGame.WORLD_SCALE);
		skeleton.setY(bodyAttachment.getWorldY()*SpaceGame.WORLD_SCALE-bodyAttachment.getY()-bodyAttachment.getHeight()*bodyAttachment.getScaleY()/2);
		Bone bodyBone = skeleton.findBone("body");
		animation.apply(skeleton, time, true);
		bodyBone.setRotation(bodyAttachment.body.getAngle()*MathUtils.radDeg);
		skeleton.updateWorldTransform();
		skeletonRenderer.draw(batch, skeleton);
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

	public Body getBody() {

		return bodyAttachment.body;
	}
}
