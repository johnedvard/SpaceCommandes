package com.rauma.lille;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.rauma.lille.screens.SplashScreen;

public class SpaceGame extends Game {
	private FPSLogger fpsLogger;

	@Override
	public void create() {
		fpsLogger = new FPSLogger();
		setScreen(getSplashScreen());
	}

	private Screen getSplashScreen() {
		return new SplashScreen(this);
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
}
