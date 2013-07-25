package com.rauma.lille.screens;

import com.badlogic.gdx.Screen;
import com.rauma.lille.SpaceGame;

public class AbstractScreen implements Screen {

	protected SpaceGame game;

	public AbstractScreen(SpaceGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
