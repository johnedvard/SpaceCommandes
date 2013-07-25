package com.rauma.lille.level001;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.rauma.lille.Resource;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.Utils;
import com.rauma.lille.actors.BodyImageActor;
import com.rauma.lille.stages.DefaultActorStage;

/**
 * @author frank
 * 
 */
public class Level001ActorStage extends DefaultActorStage {
	private BodyImageActor player;
	private float currentX;
	private float currentY;
	private float angle;

	public Level001ActorStage(int width, int height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);

		initLevel();

	}

	private void initLevel() {

		// bottom
		float width = SpaceGame.SCREEN_WIDTH;
		float height = 10;
		float x = 0;
		float y = 0;

		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y),
				Utils.Screen2World(width, height)));

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Utils.Screen2World(width / 2),
				Utils.Screen2World(height / 2));

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.0f;
		fixtureDef.friction = 0.9f;

		BodyImageActor bottom = new BodyImageActor("bottom", new TextureRegion(
				Resource.colorTexture, 0, 0, 64, 64), world, def, fixtureDef);
		bottom.setWidth(width);
		bottom.setHeight(height);
		addActor(bottom);

		// left
		width = 10;
		height = SpaceGame.SCREEN_HEIGHT;
		x = 0;
		y = 0;

		def.type = BodyType.StaticBody;
		def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y),
				Utils.Screen2World(width, height)));

		shape.setAsBox(Utils.Screen2World(width / 2),
				Utils.Screen2World(height / 2));

		BodyImageActor left = new BodyImageActor("left", new TextureRegion(
				Resource.colorTexture, 0, 0, 64, 64), world, def, shape, 0.0f);
		left.setWidth(width);
		left.setHeight(height);
		addActor(left);

		// right
		width = 10;
		height = SpaceGame.SCREEN_HEIGHT;
		x = SpaceGame.SCREEN_WIDTH - width;
		y = 0;

		def.type = BodyType.StaticBody;
		def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y),
				Utils.Screen2World(width, height)));

		shape.setAsBox(Utils.Screen2World(width / 2),
				Utils.Screen2World(height / 2));

		BodyImageActor right = new BodyImageActor("right", new TextureRegion(
				Resource.colorTexture, 0, 0, 64, 64), world, def, shape, 0.0f);
		right.setWidth(width);
		right.setHeight(height);
		addActor(right);

		// player

		width = 64;
		height = 64;
		x = 60;
		y = 40;
		
		def.type = BodyType.DynamicBody;
		def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y),
				Utils.Screen2World(width, height)));

		shape.dispose();

		CircleShape circle = new CircleShape();
		circle.setRadius(Utils.Screen2World(width / 2));

		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.9f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		player = new BodyImageActor("player", new TextureRegion(
				Resource.ballTexture, 0, 0, 64, 64), world, def, fixtureDef);
		player.setOrigin(width / 2, height / 2);
		player.setWidth(width);
		player.setHeight(height);
		addActor(player);

		circle.dispose();
	}

	@Override
	public void playerMoved(float knobX, float knobY, float knobPercentX,
			float knobPercentY) {
		currentX = knobPercentX;
		currentY = knobPercentY;
	}

	@Override
	public void playerAimed(float knobX, float knobY, float knobPercentX,
			float knobPercentY) {
		System.out.println("Aimed");
		angle = (float) Math.atan(knobY / knobX);
	}

	@Override
	public void draw() {
//		player.applyForce(new Vector2(currentX * 2, currentY * 5),
//				new Vector2());
		player.applyLinearImpulse(new Vector2(currentX/100, 0),
				new Vector2(),true);
		super.draw();
	}
}
