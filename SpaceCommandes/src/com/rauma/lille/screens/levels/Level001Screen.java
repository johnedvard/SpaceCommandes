package com.rauma.lille.screens.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.actors.Circle;
import com.rauma.lille.actors.Ground;
import com.rauma.lille.actors.Square;
import com.rauma.lille.screens.AbstractScreen;

public class Level001Screen extends AbstractScreen {
	private World world = new World(new Vector2(0, -10), true); 
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;

	private Stage stage;

	public Level001Screen(SpaceGame game) {
		super(game);
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				true);
		
		Gdx.input.setInputProcessor(stage);
		
		Ground ground = new Ground(world, stage);
		Circle circle = new Circle(world);
		Square square = new Square(world);
		
		stage.addActor(ground);
		stage.addActor(circle);
		stage.addActor(square);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		stage.act(delta);
		stage.draw();

		debugRenderer.render(world, stage.getCamera().combined);
		world.step(1/60f, 8, 3);
	}

	

	
}
