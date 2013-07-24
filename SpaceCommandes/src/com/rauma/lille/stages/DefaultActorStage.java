package com.rauma.lille.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.rauma.lille.SpaceGame;

/**
 * @author frank
 *
 */
public abstract class DefaultActorStage extends AbstractStage {

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	protected World world;

	public DefaultActorStage(float width, float height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		init();
	}
	
	/**
	 * will call <code>this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true)</code>;
	 */
	public DefaultActorStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
	}

	private void init() {
		world = new World(SpaceGame.WORLD_GRAVITY, true);
	}

	@Override
	public void draw() {
		super.draw();
		debugRenderer.render(world, getCamera().combined);
	}
}