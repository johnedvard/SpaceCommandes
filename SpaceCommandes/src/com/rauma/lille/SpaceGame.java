package com.rauma.lille;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.rauma.lille.screens.AbstractScreen;
import com.rauma.lille.screens.MainMenuScreen;
import com.rauma.lille.screens.SplashScreen;

public class SpaceGame extends Game {
	private FPSLogger fpsLogger;
	private Screen splashScreen;
	private Screen mainMenuScreen;
	private Screen gameScreen;
	
	@Override
	public void create() {
		fpsLogger = new FPSLogger();
		mainMenuScreen = new MainMenuScreen(this);
		splashScreen = new SplashScreen(this);
		//TODO create a real gameScreen
		gameScreen = new SplashScreen(this);
		setScreen(splashScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
		// output the current FPS
		fpsLogger.log();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	public Screen getMainMenuScreen() {
		return mainMenuScreen;
	}

	public Screen getGameScreen() {
		return gameScreen;
	}
}
