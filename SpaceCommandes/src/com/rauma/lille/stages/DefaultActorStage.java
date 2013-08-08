package com.rauma.lille.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.SpaceGameContactListener;
import com.rauma.lille.Utils;
import com.rauma.lille.actors.SimplePlayer;
import com.rauma.lille.armory.BulletFactory;

/**
 * @author frank
 * 
 */
public class DefaultActorStage extends AbstractStage {
	final short CATEGORY_PLAYER_1	=	0x0001; // 0000000000000001 in binary
	final short CATEGORY_PLAYER_2	=	0x0002; // 0000000000000010 in binary
	final short CATEGORY_SCENERY	=	0x1000; // 0001000000000000 in binary

	final short MASK_PLAYER_1 = CATEGORY_PLAYER_2 | CATEGORY_SCENERY; // or ~CATEGORY_PLAYER
	final short MASK_PLAYER_2 = CATEGORY_PLAYER_1 | CATEGORY_SCENERY; // or ~CATEGORY_MONSTER
	final short MASK_SCENERY = -1;

	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;

	protected World world;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	private SimplePlayer player1;
	private SimplePlayer player2;
	
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

		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		OrthographicCamera camera = (OrthographicCamera) getCamera();
		// camera.setToOrtho(false, 10f*aspectRatio, 10f);
		camera.setToOrtho(false, width, height);
		debugMatrix = camera.combined.cpy();
		debugMatrix.scale(SpaceGame.WORLD_SCALE, SpaceGame.WORLD_SCALE, 1f);

		world.setContactListener(new SpaceGameContactListener());
	}

	public void initMap(String mapName) {

		// TODO(frank): decide which is better for us
		// assetManager = new AssetManager();
		// assetManager.setLoader(TiledMap.class, new TmxMapLoader(new
		// InternalFileHandleResolver()));
		// assetManager.load("data/myFirstMap.tmx", TiledMap.class);
		// assetManager.finishLoading();
		// map = assetManager.get("data/myFirstMap.tmx");

		map = new TmxMapLoader().load(mapName);
		renderer = new OrthogonalTiledMapRenderer(map, 1f);

		renderer.setView((OrthographicCamera) getCamera());

		BodyDef def = new BodyDef();
		MapLayer box2dLayer = map.getLayers().get("box2d");
		MapObjects box2dObjects = box2dLayer.getObjects();

		PolygonShape shape = new PolygonShape();
		def.type = BodyType.StaticBody;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 0.0f;
		fixtureDef.friction = 1.0f;
		fixtureDef.restitution = 0.3f; // Make it bounce a little bit
		fixtureDef.filter.categoryBits = CATEGORY_SCENERY;
		fixtureDef.filter.maskBits = MASK_SCENERY;

		for (MapObject mapObject : box2dObjects) {
			if (mapObject instanceof PolygonMapObject) {
				PolygonMapObject polyObj = (PolygonMapObject) mapObject;
				Polygon moShape = polyObj.getPolygon();
				def.position.set(Utils.Screen2World(moShape.getX(),
						moShape.getY()));
				shape.set(Utils.Screen2World(moShape.getVertices()));
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

			System.out.println(shape);
			fixtureDef.shape = shape;
			Body body = world.createBody(def);
			body.createFixture(fixtureDef);
			body.setUserData(this);
		}
		shape.dispose();

		player1 = spawnPlayerAtPosition("Player 1", CATEGORY_PLAYER_1, MASK_PLAYER_1, 100, 100);
		player2 = spawnPlayerAtPosition("Player 2", CATEGORY_PLAYER_2, MASK_PLAYER_2, SpaceGame.SCREEN_WIDTH-200, 100);
	}

	private SimplePlayer spawnPlayerAtPosition(String name, short categoryBits, short maskBits, float x, float y) {

		BulletFactory bulletFactory = new BulletFactory(categoryBits, maskBits, world);
		SimplePlayer simplePlayer = new SimplePlayer(name, categoryBits, maskBits, x, y, world, bulletFactory);

		addActor(simplePlayer);
		return simplePlayer;
	}

	public void playerMoved(float knobX, float knobY, float knobPercentX,
			float knobPercentY) {
		currentX = knobPercentX;
		currentY = knobPercentY;
	}

	public void playerAimed(float knobX, float knobY, float knobPercentX,
			float knobPercentY) {

		angleRad = (float) Math.atan2(-knobPercentY, knobPercentX);
	}

	public void updatePlayer(float delta) {
		Vector2 linearVelocity = player1.getBody().getLinearVelocity();

		if (currentX != 0) {
			// set speed to [0, 2>
			player1.getBody().setLinearVelocity(currentX * 2, linearVelocity.y);
		}

		if (currentY > 0) {
			player1.getBody().applyForceToCenter(
					new Vector2(0, (float) (currentY * .1)), true);
		}

		if (angleRad != 0 && player1.getRotation() != angleRad) {
			player1.fireWeapon(angleRad+MathUtils.PI/2); // adjusted +90 deg
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		updatePlayer(delta);
	}

	@Override
	public void draw() {
		super.draw();

		getCamera().update();
		renderer.render();
		debugRenderer.render(world, debugMatrix);
		world.step(1 / 45f, 6, 2);
	}

}