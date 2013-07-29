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
	SkeletonRenderer skeletonRenderer; // The object used to draw the skeleton

	public Player() {
		atlas = new TextureAtlas(Gdx.files.internal("spineboy.atlas"));
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
		
		
	}
	public void draw(SpriteBatch batch, float parentAlpha) {
		skeleton.updateWorldTransform();
		skeletonRenderer.draw(batch, skeleton);
	};

	static class Box2dAttachment extends RegionAttachment {
		Body body;
		public Box2dAttachment (String name) {
			super(name);
		}
	}
}
