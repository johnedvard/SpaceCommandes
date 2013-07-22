package com.rauma.lille;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
<<<<<<< Upstream, based on origin/master
import com.rauma.lille.screens.AbstractScreen;
import com.rauma.lille.screens.MainMenuScreen;
=======
import com.badlogic.gdx.math.Vector2;
>>>>>>> f4db111 refactoring and restructuring, now nothing works, it think
import com.rauma.lille.screens.SplashScreen;

public class SpaceGame extends Game {
	private FPSLogger fpsLogger;
<<<<<<< Upstream, based on origin/master
	private Screen splashScreen;
	private Screen mainMenuScreen;
	private Screen gameScreen;
	
=======

	public static final float SCREEN_WIDTH = 480;
	public static final float SCREEN_HEIGHT = 800;

	public static final float WORLD_SCALE = 100;
	public static final Vector2 WORLD_GRAVITY = new Vector2(0f, -10f);

>>>>>>> f4db111 refactoring and restructuring, now nothing works, it think
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
