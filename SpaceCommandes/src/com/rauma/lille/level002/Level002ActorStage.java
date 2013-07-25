package com.rauma.lille.level002;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
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
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
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
 * This class is responsible for maintaining and drawing actors in the game, and
 * correlate them to the physics engine.
 * 
 * @author frank
 * 
 */
public class Level002ActorStage extends DefaultActorStage {

	BodyDef def = new BodyDef();
	
	public Level002ActorStage(int width, int height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		initMap("data/myFirstMap.tmx");
		initFrame();
	}

	
	
	private void initFrame() {
		// player
		float width = 64;
		float height = 64;
		float x = 100;
		float y = 100;

		def.type = BodyType.DynamicBody;
		def.position.set(Utils.Screen2World(x), Utils.Screen2World(y));
		CircleShape circle = new CircleShape();
		circle.setRadius(Utils.Screen2World(width / 2));
		
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.9f; 
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = .7f; // Make it bounce a little bit
        
        BodyImageActor myCircle = new BodyImageActor("circle", new TextureRegion(Resource.ballTexture, 0, 0, 64, 64), world, def, fixtureDef);
        myCircle.setWidth(width);
        myCircle.setHeight(height);
        myCircle.setOrigin(width/2, height/2);
        addActor(myCircle);
        
        circle.dispose();
	}
	@Override
	public void draw() {
		super.draw();
	}
	
}
