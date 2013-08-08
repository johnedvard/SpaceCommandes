package com.rauma.lille.actors;

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
	private static final float FIRE_RATE = 0.2f;
	private float lastFired;
	private BulletFactory bulletFactory;
	
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
			} else {
				System.out.println("Out of ammo / Weapon needs cool down");
			}
		}
	}
}
