package com.rauma.lille.level001;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.rauma.lille.Resource;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.Utils;
import com.rauma.lille.actors.BodyImageActor;
import com.rauma.lille.stages.DefaultActorStage;
import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

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

		initMap("data/test.tmx");
		initLevel();
	}

	private void initLevel() {

		// player
		float width = 64;
		float height = 64;
		float x = 100;
		float y = 100;

		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(Utils.Screen2World(x), Utils.Screen2World(y));
		CircleShape circle = new CircleShape();
		circle.setRadius(Utils.Screen2World(width / 2));

		FixtureDef fixtureDef = new FixtureDef();
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
		if(currentX != 0)
			player.setLinearVelocity(new Vector2(currentX*2,0));

		if(currentY > 0)
			player.applyForce(new Vector2(0, currentY * 6));

		super.draw();
	}
}
