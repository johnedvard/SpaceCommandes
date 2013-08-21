package com.rauma.lille.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.Utils;
import com.rauma.lille.armory.BulletFactory;

/**
 * @author frank
 * 
 */
public class Bullet extends Actor {
	private Body body = null;
	private BulletFactory bulletFactory;
	private boolean touching;
	private float damage = 5.0f;
	private int fireCount = 0;
	private int firedByPlayerId = -1;

	public Bullet(String name, short categoryBits, short maskBits, World world,
			BulletFactory bulletFactory, int firedByPlayerId) {
		this.bulletFactory = bulletFactory;
		this.firedByPlayerId = firedByPlayerId;

		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.bullet = true;
		def.position.x = Utils.Screen2World(MathUtils.random(SpaceGame.VIRTUAL_WIDTH) * 1000);
		def.position.y = Utils.Screen2World(MathUtils.random(SpaceGame.VIRTUAL_HEIGHT) * 1000);
		def.active = false;
		// def.awake = false;

		CircleShape circle = new CircleShape();
		circle.setRadius(Utils.Screen2World(1f));

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.002f;
		fixtureDef.filter.categoryBits = categoryBits;
		fixtureDef.filter.maskBits = maskBits;

		body = world.createBody(def);
		body.createFixture(fixtureDef);
		body.setUserData(this);

		setName(name);
	}

	public void fire(float x, float y, float angleRad) {
		getBody().setTransform(Utils.Screen2World(new Vector2(x, y)), angleRad);
//		getBody().setTransform(Utils.Screen2World(new Vector2(x, y)), 0);
		getBody().setActive(true);
		getBody().applyLinearImpulse(Utils.getVector(angleRad, 0.000004f),
				getBody().getWorldCenter(), true);
		fireCount++;
	}

	public void beginContact(Object other) {
		touching = true;
		// inflict damage on other
	}

	public void endContact(Object other) {
		touching = false;
	}

	private void deactivate() {
		touching = false;
		getBody().setActive(false);
		getBody().setTransform(-10, -10, 0);
		bulletFactory.release(this);
	}

	public void destroyBody() {
		if (getBody() != null) {
			World world = body.getWorld();
			world.destroyBody(body);
			body = null;
		}
	}

	public Body getBody() {
		return body;
	}
	
	public boolean isTouching() {
		return touching;
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		if (getBody() != null) {
//			setRotation(MathUtils.radDeg * getBody().getAngle());
			setX(getBody().getPosition().x * SpaceGame.WORLD_SCALE - getWidth() / 2);
			setY(getBody().getPosition().y * SpaceGame.WORLD_SCALE - getHeight() / 2);
		}

		super.draw(batch, parentAlpha);
	}

	@Override
	public void act(float delta) {
		if (touching) {
			deactivate();
		}
		super.act(delta);
	}

	@Override
	protected void finalize() throws Throwable {
		this.destroyBody();
		super.finalize();
	}

	@Override
	public String toString() {
		return super.toString() + " touching="+ isTouching() + ", awake=" + getBody().isAwake() + ", active=" + getBody().isActive() + ", fireCount="+fireCount;
	}

	public float getDamage() {
		return damage;
	}

	public int getFiredByPlayerId() {
		return firedByPlayerId;
	}
}
