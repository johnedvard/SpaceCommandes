package com.rauma.lille.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.rauma.lille.SpaceGame;

public class BodyImageActor extends Image {

	private Body body = null;

	public BodyImageActor(String name, TextureRegion texture, World world,
			BodyDef def, Shape shape, float density) {
		super(new TextureRegion(texture));
		setName(name);
		
		body = world.createBody(def);
		body.createFixture(shape, density);
		body.setUserData(this);
	}

	public BodyImageActor(String name, TextureRegion texture, World world,
			BodyDef def, FixtureDef fixturedef) {
		super(new TextureRegion(texture));
		setName(name);
		body = world.createBody(def);
		body.createFixture(fixturedef);
		body.setUserData(this);
	}

	public void destroyBody() {
		if (body != null) {
			World world = body.getWorld();
			world.destroyBody(body);
			body = null;
		}
	}

	public Body getBody() {
		return body;
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.destroyBody();
		super.finalize();
	}

	public void applyForce(Vector2 force, Vector2 point) {
		if (body != null) {
			body.applyForce(force, point, true);// Bugs? should use World
												// coordinate?
		}
	}
	public void applyLinearImpulse(Vector2 impulse, Vector2 point, boolean wake){
		if(body != null){
			body.applyLinearImpulse(impulse, point, wake);
		}
	}
	
	public float getBodyMass() {
		if (body != null) {
			return body.getMass();
		} else {
			return 0.0f;
		}
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		if (body != null) {
			setRotation(MathUtils.radDeg * body.getAngle());
			setX(body.getPosition().x * SpaceGame.WORLD_SCALE - getWidth() / 2);
			setY(body.getPosition().y * SpaceGame.WORLD_SCALE - getHeight() / 2);
		}

		super.draw(batch, parentAlpha);
	}
}