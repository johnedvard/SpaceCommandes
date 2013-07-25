package com.rauma.lille.stages;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.rauma.lille.Resource;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.Utils;
import com.rauma.lille.actors.BodyImageActor;

/**
 * @author frank
 * 
 */
public class DefaultActorStage extends AbstractStage {

	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;

	protected World world;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	private BodyImageActor player;
	private float currentX;
	private float currentY;
	private float angleRad;

	public DefaultActorStage(float width, float height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		init();
	}

	private void init() {
		world = new World(SpaceGame.WORLD_GRAVITY, true);
		debugRenderer = new Box2DDebugRenderer();
		getCamera().update();
		debugMatrix = getCamera().combined.cpy();
		debugMatrix.scale(SpaceGame.WORLD_SCALE, SpaceGame.WORLD_SCALE, 1f);
	}

	public void initMap(String mapName) {

		// TODO(frank): decide which is better for us
		// assetManager = new AssetManager();
		// assetManager.setLoader(TiledMap.class, new TmxMapLoader(new
		// InternalFileHandleResolver()));
		// assetManager.load("data/myFirstMap.tmx", TiledMap.class);
		// assetManager.finishLoading();
		// map = assetManager.get("data/myFirstMap.tmx");
		// renderer = new OrthogonalTiledMapRenderer(map, 1f);

		map = new TmxMapLoader().load(mapName);
		renderer = new OrthogonalTiledMapRenderer(map, 1f);

		BodyDef def = new BodyDef();
		MapLayer box2dLayer = map.getLayers().get("box2d");
		MapObjects box2dObjects = box2dLayer.getObjects();

		PolygonShape shape = new PolygonShape();
		def.type = BodyType.StaticBody;
		for (MapObject mapObject : box2dObjects) {
			System.out.println("adding box2d object: " + mapObject);
			if (mapObject instanceof PolygonMapObject) {
				PolygonMapObject polyObj = (PolygonMapObject) mapObject;
				Polygon moShape = polyObj.getPolygon();
				def.position.set(Utils.Screen2World(moShape.getOriginX(),
						moShape.getOriginY()));
				shape.set(Utils.Screen2World(moShape.getVertices()));
				System.out.println(shape);
			} else if (mapObject instanceof RectangleMapObject) {
				RectangleMapObject rectObj = (RectangleMapObject) mapObject;
				Rectangle moShape = rectObj.getRectangle();
				def.position.set(Utils.getWorldBoxCenter(
						Utils.Screen2World(moShape.getX(), moShape.getY()),
						Utils.Screen2World(moShape.getWidth(),
								moShape.getHeight())));
				shape.setAsBox(Utils.Screen2World(moShape.width) / 2,
						Utils.Screen2World(moShape.height) / 2);
			}

			world.createBody(def).createFixture(shape, 0.0f);
		}
		shape.dispose();

		spawnPlayer();
	}

	private void spawnPlayer() {

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
		player.getBody().setFixedRotation(true);
		addActor(player);

		circle.dispose();

	}

	public void playerMoved(float knobX, float knobY, float knobPercentX,
			float knobPercentY) {
		currentX = knobPercentX;
		currentY = knobPercentY;
	}

	public void playerAimed(float knobX, float knobY, float knobPercentX,
			float knobPercentY) {
		
		angleRad = (float) Math.atan2(knobPercentY, knobPercentX);
//		angleRad += Math.PI/2.0;
//		angle = (float) Math.toDegrees(theta);
//
//		if (angle < 0) {
//			angle += 360;
//		}

		System.out.println("Aimed " + knobPercentX + ", " + knobPercentY + " -> " + angleRad);
	}

	public void updatePlayer() {
		if (currentX != 0) {
			player.setLinearVelocity(new Vector2(currentX * 2, 0));
		}

		if (currentY > 0) {
			player.applyForce(new Vector2(0, currentY * 6));
		}

		if (angleRad != 0 && player.getRotation() != angleRad) {
			player.getBody().setTransform(player.getBody().getPosition(),
					angleRad);
		}
		super.draw();
	}

	@Override
	public void draw() {
		super.draw();
		updatePlayer();
		OrthographicCamera camera = (OrthographicCamera) getCamera();
		camera.update();
		renderer.setView(camera);
		renderer.render();

		debugRenderer.render(world, debugMatrix);
		world.step(1 / 45f, 6, 2);
	}

}