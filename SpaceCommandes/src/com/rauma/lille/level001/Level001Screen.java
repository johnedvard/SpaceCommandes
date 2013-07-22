package com.rauma.lille.level001;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.screens.AbstractScreen;
import com.rauma.lille.stages.ActorStage;
import com.rauma.lille.stages.BackgroundStage;
import com.rauma.lille.stages.UIStage;

public class Level001Screen extends AbstractScreen {

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	private BackgroundStage backgroundStage;
	private ActorStage actorStage;
	private UIStage uiStage;

	public Level001Screen(SpaceGame game) {
		super(game);
		
		backgroundStage = new BackgroundStage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), true);
		actorStage = new ActorStage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), true);
		uiStage = new UIStage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), true);

		Gdx.input.setInputProcessor(actorStage);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		
		actorStage.act(delta);
		backgroundStage.act(delta);
		uiStage.act();
		
		actorStage.draw();
		backgroundStage.draw();
		uiStage.draw();
	}

}
