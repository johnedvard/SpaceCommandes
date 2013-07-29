package com.rauma.lille;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Skin;
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

	public Player() {
		atlas = new TextureAtlas(Gdx.files.internal("spineboy.atlas"));
		SkeletonJson json = new SkeletonJson(atlas);
		SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("spineboy.json"));
		json.setScale(0.05f);
		animation = skeletonData.findAnimation("walk");
		skeleton = new Skeleton(skeletonData);
		skeleton.setX(50);
		skeleton.setY(50);
		skeleton.updateWorldTransform();
		
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
	};
}
