package com.rauma.lille.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.Utils;

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

	protected void initMap(String mapName) {

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
				def.position.set(Utils.Screen2World(moShape.getOriginX(), moShape.getOriginY()));
				shape.set(Utils.Screen2World(moShape.getVertices()));
				System.out.println(shape);
			} else if (mapObject instanceof RectangleMapObject) {
				RectangleMapObject rectObj = (RectangleMapObject) mapObject;
				Rectangle moShape = rectObj.getRectangle();
				def.position.set(Utils.getWorldBoxCenter(Utils.Screen2World(moShape.getX(), moShape.getY()), Utils.Screen2World(moShape.getWidth(), moShape.getHeight())));
				shape.setAsBox(Utils.Screen2World(moShape.width)/2, Utils.Screen2World(moShape.height)/2);
			}
			
			world.createBody(def).createFixture(shape, 0.0f);
		}
		shape.dispose();		
	}
	
	@Override
	public void draw() {
		super.draw();
		
		OrthographicCamera camera = (OrthographicCamera) getCamera();
		camera.update();
		renderer.setView(camera);
		renderer.render();
		
		debugRenderer.render(world, debugMatrix);
		world.step(1 / 45f, 6, 2);
	}

	public void playerMoved(float knobX, float knobY, float knobPercentX,
			float knobPercentY) {
	}

	public void playerAimed(float knobX, float knobY, float knobPercentX,
			float knobPercentY) {
	}
}