package com.rauma.lille.screens;

import com.badlogic.gdx.Gdx;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.stages.BackgroundStage;
import com.rauma.lille.stages.ControllerStage;
import com.rauma.lille.stages.DefaultActorStage;
import com.rauma.lille.stages.UIStage;

public class DefaultLevelScreen extends AbstractScreen {

	protected ControllerStage controllerStage;
	protected DefaultActorStage actorStage;
	protected BackgroundStage bgStage;
	protected UIStage uiStage;

	public DefaultLevelScreen(SpaceGame game) {
		super(game);
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getWidth();
		controllerStage = new ControllerStage(width, height, true);
		actorStage = new DefaultActorStage(width, height, true);
		bgStage = new BackgroundStage(width, height, true);
		uiStage =  new UIStage(width, height, true);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(controllerStage);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		bgStage.draw();
		actorStage.draw();
		uiStage.draw();
		controllerStage.draw();
	}
}