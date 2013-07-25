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

	private AssetManager assetManager;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	private BodyDef def;
	private PolygonShape shape;

	public Level002ActorStage(int width, int height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		def = new BodyDef();
        def.type = BodyType.StaticBody;
        shape = new PolygonShape();
		initTiledMap();
		initFrame();
        shape.dispose();
	}

	
	// draw the textures from the map made in Tiled
	// add the objects for collision detection as well.
	private void initTiledMap() {
		
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load("data/myFirstMap.tmx", TiledMap.class);
		assetManager.finishLoading();
		map = assetManager.get("data/myFirstMap.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1f);
		
		// add the objects
		MapLayer layer = map.getLayers().get("Object");
		MapObjects objects = layer.getObjects();
		for(MapObject o : objects){
			System.out.println(o.getName());
			if(o instanceof RectangleMapObject){
				RectangleMapObject rectangleMapObject = (RectangleMapObject) o;
				Rectangle rectangle = rectangleMapObject.getRectangle();
				System.out.println(rectangle);
				def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(rectangle.getX(), rectangle.getY()), Utils.Screen2World(rectangle.getWidth(), rectangle.getHeight())));
				shape.setAsBox(Utils.Screen2World(rectangle.width)/2, Utils.Screen2World(rectangle.height)/2);
				world.createBody(def).createFixture(shape, 0.0f);
			}
		}
	}
	
	private void initFrame() {
                        
        //bottom
        float width = SpaceGame.SCREEN_WIDTH;
        float height = 10;
        float x = 0;
        float y = 0;
        
        //Bottom
        shape.setAsBox(Utils.Screen2World(width / 2), Utils.Screen2World(height / 2));
        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));

        BodyImageActor bottom = new BodyImageActor("bottom", new TextureRegion(Resource.colorTexture, 0, 0, 64, 64), world, def, shape, 1.0f);
        bottom.setWidth(width);
        bottom.setHeight(height);
        addActor(bottom);
        
        //top
        y= SpaceGame.SCREEN_HEIGHT - height;
        x = 0;
        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));
        BodyImageActor top = new BodyImageActor("top", new TextureRegion(Resource.colorTexture, 0, 64 * 3, 64, 64), world, def, shape, 1.0f);
        top.setWidth(width);
        top.setHeight(height);
        addActor(top);
        
        //left
        width = 10;
        height = SpaceGame.SCREEN_HEIGHT;
        x = 0;
        y = 0;
        
        shape.setAsBox(Utils.Screen2World(width / 2), Utils.Screen2World(height / 2));      
        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));
        BodyImageActor left = new BodyImageActor("left", new TextureRegion(Resource.colorTexture, 0, 64, 64, 64), world, def, shape, 1.0f);
        left.setWidth(width);
        left.setHeight(height);
        
        addActor(left);
        
        //right
        width = 10;
        height = SpaceGame.SCREEN_HEIGHT;
        x = SpaceGame.SCREEN_WIDTH - width;
        y = 0;
        
        shape.setAsBox(Utils.Screen2World(width / 2), Utils.Screen2World(height / 2));      
        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));

        BodyImageActor right = new BodyImageActor("right", new TextureRegion(Resource.colorTexture, 0, 64 * 2, 64, 64), world, def, shape, 1.0f);
        right.setWidth(width);
        right.setHeight(height);
        addActor(right);

        x = 400;
        y = 100;
        width = 62;
        height = 62;
     // Create our body in the world using our body definition
        def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(x, y), Utils.Screen2World(width, height)));
        def.type = BodyType.DynamicBody;
        
     // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(Utils.Screen2World(height/2));
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
        myCircle.applyForce(Utils.Screen2World(400,0), new Vector2(0,0));
        
        circle.dispose();
	}
	@Override
	public void draw() {
		super.draw();
		// draw the map
		renderer.setView((OrthographicCamera) getCamera());
		renderer.render();
	}
	
}
