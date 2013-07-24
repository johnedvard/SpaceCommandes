package com.rauma.lille.stages;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.rauma.lille.SpaceGame;

/**
 * @author frank
 * 
 */
public class DefaultActorStage extends AbstractStage {

	private Box2DDebugRenderer debugRenderer;
	protected World world;
	private Matrix4 debugMatrix;

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

	@Override
	public void draw() {
		super.draw();
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