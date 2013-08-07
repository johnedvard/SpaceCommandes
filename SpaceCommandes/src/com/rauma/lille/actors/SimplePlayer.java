package com.rauma.lille.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.rauma.lille.BulletFactory;

public class SimplePlayer extends BodyImageActor {
	private static final float FIRE_RATE = 0.1f;
	private float lastFired;

	public SimplePlayer(String name, TextureRegion texture, World world,
			BodyDef def, FixtureDef fixtureDef) {
		super(name, texture, world, def, fixtureDef);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		lastFired += delta;
	}

	public void fireWeapon(float angleRad) {
		// player.getBody().setTransform(player.getBody().getPosition(),
		// angleRad);
		if (lastFired >= FIRE_RATE) {
			Bullet bullet = BulletFactory.getBullet();
			if (bullet != null) {
				bullet.fire(getX()+getWidth()+1, getY()+getHeight()/2, angleRad);
				lastFired = 0f;
			}
		}
	}
}
