package com.rauma.lille.stages;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.rauma.lille.SpaceGameContactListener;
import com.rauma.lille.BulletFactory;
import com.rauma.lille.Resource;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.Utils;
import com.rauma.lille.actors.SimplePlayer;

/**
 * @author frank
 * 
 */
public class DefaultActorStage extends AbstractStage {
	final short CATEGORY_PLAYER = 0x0001; // 0000000000000001 in binary
	final short CATEGORY_MONSTER = 0x0002; // 0000000000000010 in binary
	final short CATEGORY_SCENERY = 0x0004; // 0000000000000100 in binary

	final short MASK_PLAYER = CATEGORY_MONSTER | CATEGORY_SCENERY; // or
																	// ~CATEGORY_PLAYER
	final short MASK_MONSTER = CATEGORY_PLAYER | CATEGORY_SCENERY; // or
																	// ~CATEGORY_MONSTER
	final short MASK_SCENERY = -1;

	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;

	protected World world;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	private SimplePlayer player;
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
		float aspectRatio = (float) width / (float) height;

		BulletFactory.init(world);

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
			world.createBody(def).createFixture(fixtureDef);
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
		fixtureDef.density = 0.0195f;
		fixtureDef.friction = 1.0f;
		fixtureDef.restitution = 0.3f; // Make it bounce a little bit
		fixtureDef.filter.categoryBits = CATEGORY_PLAYER;
		fixtureDef.filter.maskBits = MASK_PLAYER;

		player = new SimplePlayer("player", new TextureRegion(
				Resource.ballTexture, 0, 0, 64, 64), world, def, fixtureDef);
		player.setOrigin(width / 2, height / 2);
		player.setWidth(width);
		player.setHeight(height);
		// player.getBody().setFixedRotation(true);
		player.getBody().setAngularDamping(10.0f);

		addActor(player);

		circle.setRadius(0.01f);
		fixtureDef.shape = circle;
		fixtureDef.density = 0.00001f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.0f;
		fixtureDef.filter.categoryBits = CATEGORY_PLAYER;
		fixtureDef.filter.maskBits = MASK_PLAYER;

		circle.dispose();
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
		Vector2 linearVelocity = player.getBody().getLinearVelocity();

		if (currentX != 0) {
			// set speed to [0, 2>
			player.getBody().setLinearVelocity(currentX * 2, linearVelocity.y);
		}

		if (currentY > 0) {
			player.getBody().applyForceToCenter(
					new Vector2(0, (float) (currentY * .1)), true);
		}

		if (angleRad != 0 && player.getRotation() != angleRad) {
			player.fireWeapon(angleRad+MathUtils.PI/2); // adjusted +90 deg
		}
	}
	
	public Actor getPlayer(){
		return player;
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
		BulletFactory.deactivateFreeBullets();
		debugRenderer.render(world, debugMatrix);
		world.step(1 / 45f, 6, 2);
	}

}