package com.rauma.lille.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.rauma.lille.Resource;
import com.rauma.lille.Utils;
import com.rauma.lille.armory.BulletFactory;

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
	private float health = 100;;
	
	public SimplePlayer(String name, short categoryBits, short maskBits, float x, float y, World world, BulletFactory bulletFactory) {
		super(new TextureRegion(Resource.ballTexture, 0, 0, 64, 64));
		this.bulletFactory = bulletFactory;

		// player
		float width = 64;
		float height = 64;

		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(Utils.Screen2World(x), Utils.Screen2World(y));
		
		CircleShape circle = new CircleShape();
		circle.setRadius(Utils.Screen2World(width / 2));

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.0195f;
		fixtureDef.friction = 1.0f;
		fixtureDef.restitution = 0.3f; // Make it bounce a little bit
		fixtureDef.filter.categoryBits = categoryBits;
		fixtureDef.filter.maskBits = maskBits;

		Body body = world.createBody(def);
		body.createFixture(fixtureDef);
		body.setUserData(this);
		// body.setFixedRotation(true);
		body.setAngularDamping(10.0f);
		setBody(body);
		
		setName(name);
		setOrigin(width / 2, height / 2);
		setWidth(width);
		setHeight(height);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if(health < 1) {
			die();
		}
		lastFired += delta;
	}

	public void fireWeapon(float angleRad) {
		// player.getBody().setTransform(player.getBody().getPosition(),
		// angleRad);
		if (lastFired >= FIRE_RATE) {
			Bullet bullet = bulletFactory.getBullet();
			if (bullet != null) {
				getStage().addActor(bullet);
				bullet.fire(getX()+getWidth()+5, getY()+getHeight()/2, angleRad);
				lastFired = 0f;
				successfulShot++;
			} else {
				System.out.println("Out of ammo / Weapon needs cool down");
				failedShot++;
			}
		}
	}

	public void applyDamage(float damage) {
		this.health -= damage;
		System.out.println("New health: " + health);
	}
	
	private void die() {
		destroyBody();
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
//		batch.end(); // actual drawing is done on end(); if we do not end, we contaminate previous rendering.
//		batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
//		batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//		batch.begin();
		Color newColor = getColor();
		newColor.a = health/100;
		setColor(newColor);
		super.draw(batch, parentAlpha);
	}
}
