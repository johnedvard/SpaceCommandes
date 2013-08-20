package com.rauma.lille.stages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.rauma.lille.network.ApplyDamageCommand;
import com.rauma.lille.network.Command;
import com.rauma.lille.network.KillCommand;
import com.rauma.lille.network.PositionCommand;
import com.rauma.lille.network.StartGameCommand;

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
	
	private float currentX;
	private float currentY;
	private List<Command> commands = new ArrayList<Command>();
	private Map<String,Vector2> spawnpoints = new HashMap<String,Vector2>();
	private SpaceGame game;
	private List<PositionCommand> updatePositions = new ArrayList<PositionCommand>();

	private int playerId = -1;
	private boolean newGame = false;
	private int numberOfPlayers = -1;
	private String spawnPointName = null;
	private List<SimplePlayer> otherPlayers = new ArrayList<SimplePlayer>();

	public DefaultActorStage(SpaceGame game, float width, float height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		this.game = game;
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
		MapLayer spawnLayer = map.getLayers().get("spawn");
		MapObjects box2dObjects = box2dLayer.getObjects();
		MapObjects spawnPoints = spawnLayer.getObjects();

		PolygonShape shape = new PolygonShape();
		def.type = BodyType.StaticBody;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 0.0f;
		fixtureDef.friction = 0.1f;
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
		for(MapObject spawnPoint : spawnPoints){
			if(spawnPoint instanceof RectangleMapObject){
				Rectangle spawnRectangle = ((RectangleMapObject) spawnPoint).getRectangle();
				spawnpoints.put(spawnPoint.getName(), new Vector2(spawnRectangle.getX(),spawnRectangle.getY()).scl(unitScale));
			}
		}
		
		shape.dispose();

		player1 = spawnPlayerAtPosition(-1,"Player 1", CATEGORY_PLAYER_1, MASK_PLAYER_1, 100, 100,false,true);
	}

	private SimplePlayer spawnPlayerAtPosition(int playerId, String name, short categoryBits, short maskBits, float x, float y,boolean isStaticBody, boolean isMe) {
		BulletFactory bulletFactory = new BulletFactory(categoryBits, maskBits, world);
		SimplePlayer simplePlayer = new SimplePlayer(playerId, name, categoryBits, maskBits, x, y, world, bulletFactory, isStaticBody, game, isMe);
		addActor(simplePlayer);
		return simplePlayer;
	}

	public void playerMoved(float knobX, float knobY, float knobPercentX,
			float knobPercentY) {
		currentX = knobPercentX;
		currentY = knobPercentY;
	}

	public void playerAimed(int playerId, float knobX, float knobY, float knobPercentX,
			float knobPercentY) {
		float angleRad = (float) Math.atan2(-knobPercentY, knobPercentX);
		if(player1.getId() == playerId){
			player1.setAngleRad(angleRad);
		}
		else{
			for(SimplePlayer sp : otherPlayers){
				if(sp.getId() == playerId){
					sp.setAngleRad(angleRad);
				}
			}
		}
	}

	public void updatePlayer(SimplePlayer player, float delta) {
		if(player == null || player.getBody() == null){
			//TODO (john) Cast an error?
			return;
		}
		Vector2 linearVelocity = player.getBody().getLinearVelocity();

		if (currentX != 0) {
			// set speed to [0, 2>
			player.getBody().setLinearVelocity(currentX * 2, linearVelocity.y);
		}

		if (currentY > 0) {
			float minPower = 0.8f;
			if(currentY < minPower)
				currentY = minPower;
			//
			if(player.getBody().getLinearVelocity().y == 0){
				player1.setAnimation("fly-in");
			}else{
				player1.setAnimation("fly");
			}
			player.getBody().applyForceToCenter(
					new Vector2(0, (float) (currentY * .1)), true);
		}else if(player.getBody().getLinearVelocity().y == 0){
			player1.setAnimation("idle");
		}

		if (player.getAngleRad() != 0 && player.getRotation() != player.getAngleRad()) {
			player.fireWeapon(player.getAngleRad()+MathUtils.PI/2); // adjusted +90 deg
		}
	}
	
	public SimplePlayer getPlayer(){
		return player1;
	}

	@Override
	public void act(float delta) {
		if(newGame) {

			for(Actor a : this.getActors()){
				if (a instanceof BodyImageActor) {
					BodyImageActor bodyActor = (BodyImageActor) a;
					bodyActor.destroyBody();
					bodyActor.remove();
				}
			}
			
			for(int i = 0; i<numberOfPlayers; i++){
				if(i == playerId){
					Vector2 s = spawnpoints.get(spawnPointName);
					System.out.println("vector2: with name: " + spawnPointName);
					System.out.println(s);
					player1 = spawnPlayerAtPosition(playerId,"Player "+playerId, CATEGORY_PLAYER_1, MASK_PLAYER_1, s.x, s.y, false,true);
				}else{
					otherPlayers.add(spawnPlayerAtPosition(i, "Player "+i, CATEGORY_PLAYER_2, MASK_PLAYER_2, 400, 150, true, false));
				}
			}
			newGame = false;
		}
		super.act(delta);
		updatePlayer(player1, delta);
		updateOtherPlayers(delta);
		executeCommandQueue();
		getCamera().position.set(getPlayerPosition());
		getCamera().update();
		debugMatrix = getCamera().combined.cpy();
		debugMatrix.scale(SpaceGame.WORLD_SCALE, SpaceGame.WORLD_SCALE, 1f);
		renderer.setView((OrthographicCamera) getCamera());
	}

	private void updateOtherPlayers(float delta) {
		for(SimplePlayer sp : otherPlayers){
			updatePlayer(sp, delta);
		}
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
		playerId = startGameCommand.getPlayerId();
		numberOfPlayers = startGameCommand.getNumPlayers();
		spawnPointName = startGameCommand.getSpawnPointName();
		newGame = true;
	}

	private void executeCommandQueue(){
		while(commands.size()>0){
			Command command = commands.remove(0);
			if (command instanceof PositionCommand) {
				PositionCommand commandPos = (PositionCommand) command;
				
				int id = commandPos.getId();
				float x = commandPos.getX();
				float y = commandPos.getY();
				float angle = commandPos.getAngle();
				if(id == -1){
					// can come into this state if the game isn't initialized yet
					//TODO (john) cast an error?
					return;
				}
				
				if(player1.getId() != id){
					moveOtherPlayers(x, y, angle);
				}
				
			}else if (command instanceof KillCommand) {
				KillCommand killCommand = (KillCommand) command;
				killPlayer(killCommand);
			}
		}
	}
	
	private void moveOtherPlayers(float x, float y, float angle) {
		for(SimplePlayer sp : otherPlayers){
			if (sp != null && sp.getBody() != null) {
				Body body = sp.getBody();
				Transform transform = body.getTransform();
				Vector2 position = transform.getPosition();
				float newX = x + sp.getWidth() / 2;
				float newY = y + sp.getHeight() / 2;
				if (position.x != newX && position.y != newY) {
					body.setTransform(Utils.Screen2World(newX, newY), angle);
				}
			}
		}
	}

	private void killPlayer(KillCommand killCommand) {
		int id = killCommand.getPlayerId();
		if(player1.getId() == id){
			String name = player1.getName();
			short maskBits = player1.getMaskBits();
			short categoryBits = player1.getCategoryBits();
			boolean isStaticBody = player1.isStaticBody();
			boolean isMe = player1.isMe();
			player1.destroyBody();
			player1.remove();
			Vector2 spawnPoint = spawnpoints.get("sp"+(id+1));
			player1 = spawnPlayerAtPosition(id, name, categoryBits, maskBits, spawnPoint.x, spawnPoint.y, isStaticBody, isMe);
		}
		else {
			for(SimplePlayer sp : otherPlayers){
				if(sp.getId() == id){
//					String name = sp.getName();
//					sp.destroyBody();
//					sp.remove();
//					Vector2 spawnPoint = spawnpoints.get("sp"+(sp.getId()+1));
//					System.out.println("spawingn other player");
//					sp = spawnPlayerAtPosition(sp.getId(), name, CATEGORY_PLAYER_2, MASK_PLAYER_2, spawnPoint.x, spawnPoint.y, true, false);
					sp.setHealth(100);
				}
			}
		}
	}

	public void updatePlayerPos(PositionCommand commandPos) {
		commands.add(commandPos);
	}

	public void applyDamageCommand(ApplyDamageCommand applyDmgCommand) {
		int id = applyDmgCommand.getId();
		float dmg = applyDmgCommand.getDamage();
		if(id == player1.getId()){
			player1.applyDamage(dmg);
		}
		else {
			for(SimplePlayer sp : otherPlayers){
				if(sp.getId() == id){
					sp.applyDamage(dmg);
				}
			}
		}
	}

	public void killCommand(KillCommand killCommand) {
		commands.add(killCommand);
	}
}