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
	private Box2DDebugRenderer box2dRenderer;
	Vector2 vector = new Vector2();
	private World world;
	
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
		SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("paperman.json"));
		animation = skeletonData.findAnimation("walk");
		skeleton = new Skeleton(skeletonData);
		skeleton.setX(150);
		skeleton.setY(150);
		skeleton.updateWorldTransform();
		
		// Create a body for each attachment. Note it is probably better to create just a few bodies rather than one for each
		// region attachment, but this is just an example.
		for (Slot slot : skeleton.getSlots()) {
			System.out.println(slot);
			if (!(slot.getAttachment() instanceof Box2dAttachment)) continue;
			Box2dAttachment attachment = (Box2dAttachment)slot.getAttachment();
			System.out.println("att: " + attachment + "" + attachment.getWidth());

			PolygonShape boxPoly = new PolygonShape();
			boxPoly.setAsBox(Utils.Screen2World(attachment.getWidth() * attachment.getScaleX())/2,
				Utils.Screen2World(attachment.getHeight() * attachment.getScaleY())/2,
				vector.set(Utils.Screen2World(attachment.getX(), attachment.getY())),
				attachment.getRotation() * MathUtils.degRad);

			BodyDef boxBodyDef = new BodyDef();
			boxBodyDef.type = BodyType.DynamicBody;
			attachment.body = world.createBody(boxBodyDef);
			
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = boxPoly;
			fixtureDef.density = 0.5f;
			fixtureDef.restitution = 0.1f;
			fixtureDef.friction= 0.1f;
			attachment.body.createFixture(fixtureDef);

			boxPoly.dispose();
		}
	}
	public void draw(SpriteBatch batch, float parentAlpha) {
		float delta = Gdx.graphics.getDeltaTime();
		float remaining = delta;
		while (remaining > 0) {
			float d = Math.min(0.016f, remaining);
			time += d;
			remaining -= d;
		}
		
		animation.apply(skeleton, time, true);
		skeleton.updateWorldTransform();
		skeletonRenderer.draw(batch, skeleton);
		
		// Position each attachment body.
		for (Slot slot : skeleton.getSlots()) {
			if (!(slot.getAttachment() instanceof Box2dAttachment)) continue;
			Box2dAttachment attachment = (Box2dAttachment)slot.getAttachment();
			if (attachment.body == null) continue;
			float x = Utils.Screen2World(skeleton.getX() + slot.getBone().getWorldX());
			float y = Utils.Screen2World(skeleton.getY() + slot.getBone().getWorldY());
			float rotation = slot.getBone().getWorldRotation();
			attachment.body.setTransform(x, y, rotation * MathUtils.degRad);
		}
		Box2dAttachment attachment = (Box2dAttachment) skeleton.getAttachment("sc_body", "sc_body");
		Vector2 v = attachment.body.getPosition();
		v.x +=1 ;
		attachment.body.setTransform(v, attachment.body.getAngle());
		
		
	}
	
	static class Box2dAttachment extends RegionAttachment {
		Body body;
		public Box2dAttachment (String name) {
			super(name);
		}
	}
}
