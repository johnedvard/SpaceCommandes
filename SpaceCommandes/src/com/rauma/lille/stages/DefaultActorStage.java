package com.rauma.lille.stages;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.rauma.lille.SpaceGame;

/**
 * @author frank
 *
 */
public class DefaultActorStage extends AbstractStage {

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	protected World world;

	public DefaultActorStage(float width, float height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		init();
	}

	private void init() {
		world = new World(SpaceGame.WORLD_GRAVITY, true);
	}

	@Override
	public void draw() {
		super.draw();
		world.step(1/45f, 6, 2);
		debugRenderer.render(world, getCamera().combined);
	}
}