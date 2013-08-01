package com.rauma.lille.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.rauma.lille.Utils;

/**
 * @author frank
 * 
 */
public class Bullet extends BodyImageActor {
	private boolean ready = true;

	public Bullet(String name, TextureRegion texture, World world, BodyDef def,
			Shape shape, float density) {
		super(name, texture, world, def, shape, density);
		getBody().setBullet(true);
	}

	public Bullet(String name, TextureRegion texture, World world,
			BodyDef def, FixtureDef fixtureDef) {
		super(name, texture, world, def, fixtureDef);
		getBody().setBullet(true);
	}

	public void fire(float x, float y, float angle) {
		ready = false;
		getBody().setTransform(Utils.Screen2World(new Vector2(x, y)), angle * MathUtils.degRad);
		getBody().applyLinearImpulse(Utils.Screen2World(getVector(angle * MathUtils.degRad, 0.000001f)), getBody().getWorldCenter(), true);
	}

	private Vector2 getVector(float angle, float magnitude) {
		return new Vector2((float) Math.sin(angle)*magnitude, (float) Math.cos(angle)*magnitude);
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
}
