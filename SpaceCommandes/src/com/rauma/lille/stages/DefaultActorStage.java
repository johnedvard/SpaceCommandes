package com.rauma.lille.stages;

import java.util.ArrayList;
import java.util.List;

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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.SpaceGameContactListener;
import com.rauma.lille.Utils;
import com.rauma.lille.actors.BodyImageActor;
import com.rauma.lille.actors.SimplePlayer;
import com.rauma.lille.armory.BulletFactory;
import com.rauma.lille.network.PositionCommand;
import com.rauma.lille.network.StartGameCommand;
import com.rauma.lille.screens.DefaultLevelScreen;

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
	private List<PositionCommand> updatePositions = new ArrayList<PositionCommand>();
	private DefaultLevelScreen defaultLevelScreen = null;

	public DefaultActorStage(float width, float height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		init();
	}

	private void init() {
		world = new World(SpaceGame.WORLD_GRAVITY, true);
		world.setContinuousPhysics(true);
		debugRenderer = new Box2DDebugRenderer();


		OrthographicCamera camera = (OrthographicCamera) getCamera();
		float width = getWidth();
		float height = getHeight();
//		float aspectRatio = width/height;
//		float scale = 128f;
//		camera.setToOrtho(false, scale*aspectRatio, scale);
		camera.setToOrtho(false, width/2, height/2);
//		debugMatrix.scale(SpaceGame.WORLD_SCALE*SCALE*aspectRatio, SpaceGame.WORLD_SCALE*SCALE, 1f);

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
		float unitScale = 1/2f;
		renderer = new OrthogonalTiledMapRenderer(map, unitScale);

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
				def.position.set(Utils.Screen2World(moShape.getX(),	moShape.getY()).scl(unitScale));
				float[] vertices = moShape.getVertices();
				shape.set(Utils.Screen2World(vertices, unitScale));
			} else if (mapObject instanceof RectangleMapObject) {
				RectangleMapObject rectObj = (RectangleMapObject) mapObject;
				Rectangle moShape = rectObj.getRectangle();
				def.position.set(Utils.getWorldBoxCenter(
						Utils.Screen2World(moShape.getX(), moShape.getY()).scl(unitScale),
						Utils.Screen2World(moShape.getWidth(), moShape.getHeight()).scl(unitScale)));
				shape.setAsBox(Utils.Screen2World(moShape.width) / 2 * unitScale,
						Utils.Screen2World(moShape.height) / 2 * unitScale);
			}
			System.out.println(shape);
			fixtureDef.shape = shape;
			Body body = world.createBody(def);
			body.createFixture(fixtureDef);
			body.setUserData(this);
		}
		shape.dispose();

		player1 = spawnPlayerAtPosition(-1,"Player 1", CATEGORY_PLAYER_1, MASK_PLAYER_1, 100, 100,false);
	}

	private SimplePlayer spawnPlayerAtPosition(int playerId, String name, short categoryBits, short maskBits, float x, float y,boolean isStaticBody) {
		BulletFactory bulletFactory = new BulletFactory(categoryBits, maskBits, world);
		SimplePlayer simplePlayer = new SimplePlayer(playerId, name, categoryBits, maskBits, x, y, world, bulletFactory, isStaticBody);
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
		if(player1 == null || player1.getBody() == null){
			//TODO (john) Cast an error?
			return;
		}
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
	
	public SimplePlayer getPlayer(){
		return player1;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		updatePlayer(delta);
		updatePlayerPosFromQueue();
		getCamera().position.set(getPlayerPosition());
		getCamera().update();
		debugMatrix = getCamera().combined.cpy();
		debugMatrix.scale(SpaceGame.WORLD_SCALE, SpaceGame.WORLD_SCALE, 1f);
		renderer.setView((OrthographicCamera) getCamera());
	}

	@Override
	public void draw() {
		super.draw();
		renderer.render();
		debugRenderer.render(world, debugMatrix);
		world.step(1 / 45f, 6, 2);
	}

	public Vector3 getPlayerPosition() {
		float centerX = player1.getX()+player1.getWidth()/2;
		float centerY = player1.getY()+player1.getHeight()/2;
		float minWidth = getWidth()/4;
		float maxWidth = getWidth() - minWidth;
		float minHeight = getHeight()/4;
		float maxHeight = getHeight() - minHeight;

		if(centerX < minWidth) {
			centerX = minWidth;
		} else if(centerX > maxWidth) {
			centerX = maxWidth;
		}
			
		if(centerY < minHeight) {
			centerY = minHeight;
		} else if(centerY > maxHeight) {
			centerY = maxHeight;
		}
		Vector2 coords = new Vector2(centerX, centerY);
		return new Vector3(coords.x, coords.y, 0);
	}

	public void createNewGame(StartGameCommand startGameCommand) {
		for(Actor a : this.getActors()){
			if (a instanceof BodyImageActor) {
				BodyImageActor bodyActor = (BodyImageActor) a;
				bodyActor.destroyBody();
				bodyActor.remove();
			}
		}
		//hardcoded for two players
		if(startGameCommand.getPlayerId() == 1){
			player1 = spawnPlayerAtPosition(startGameCommand.getPlayerId(),"Player 1", CATEGORY_PLAYER_1, MASK_PLAYER_1, 100, 100,false);
			player2 = spawnPlayerAtPosition(2,"Player 2", CATEGORY_PLAYER_2, MASK_PLAYER_2, 400, 150,true);
		}
		else{
			player1 = spawnPlayerAtPosition(startGameCommand.getPlayerId(),"Player 2", CATEGORY_PLAYER_2, MASK_PLAYER_2, 400, 150,false);
			player2 = spawnPlayerAtPosition(1,"Player 1", CATEGORY_PLAYER_1, MASK_PLAYER_1, 100, 100,true);
		}
	}

	private void updatePlayerPosFromQueue(){
		while(updatePositions.size()>0){
			PositionCommand commandPos = updatePositions.remove(0);
			if(commandPos == null){
				//TODO (john)(cast and error?)
				continue;
			}
			int id = commandPos.getId();
			float x = commandPos.getX();
			float y = commandPos.getY();
			if(id == -1){
				// can come into this state if the game isn't initialized yet
				//TODO (john) cast an error?
				return;
			}
			
			//hardcoded for two players
			if (player1.getId() == 1 && id == 2) {
				movePlayer2(x, y);
			} else if (player1.getId() == 2 && id == 1) {
				movePlayer2(x, y);
			}
		}
	}
	
	private void movePlayer2(float x, float y) {

		if (player2 != null && player2.getBody() != null) {
			Body body = player2.getBody();
			Transform transform = body.getTransform();
			Vector2 position = transform.getPosition();
			float newX = x + player2.getWidth() / 2;
			float newY = y + player2.getHeight() / 2;
			if (position.x != newX && position.y != newY) {
				body.setTransform(Utils.Screen2World(newX, newY), 0);
			}
		}
			
	}
	public void updatePlayerPos(PositionCommand commandPos) {
		updatePositions.add(commandPos);
	}
}