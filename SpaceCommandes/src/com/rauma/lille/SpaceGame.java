package com.rauma.lille;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Vector2;
import com.rauma.lille.screens.DefaultLevelScreen;
import com.rauma.lille.screens.MainMenuScreen;
import com.rauma.lille.screens.SplashScreen;

public class SpaceGame extends Game {
	private FPSLogger fpsLogger;
	private Screen splashScreen;
	private Screen mainMenuScreen;

	public static final float SCREEN_WIDTH = 800;
	public static final float SCREEN_HEIGHT = 480;

	public static final float WORLD_SCALE = 100;
	public static final Vector2 WORLD_GRAVITY = new Vector2(0f, -10f);

	public static boolean DEBUG = false;
	public static boolean COLLISION = true;
	public static boolean ADDACTOR = false;

	@Override
	public void create() {
		fpsLogger = new FPSLogger();
		
		Resource.initalize();
		
		splashScreen = new SplashScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		setScreen(mainMenuScreen);
	}
	
	public void startMap(String mapName) {
		DefaultLevelScreen screen = new DefaultLevelScreen(this, mapName);
		setScreen(screen);
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
}
