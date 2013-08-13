package com.rauma.lille;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;
import com.rauma.lille.screens.DefaultLevelScreen;

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
	private OutputStream outputStream;
	private InputStream inputStream;

	@Override
	public void create() {
		fpsLogger = new FPSLogger();
		
		Texture.setEnforcePotImages(false);
		Resource.initalize();
		
//		splashScreen = new SplashScreen(this);
//		mainMenuScreen = new MainMenuScreen(this);
		setScreen(new DefaultLevelScreen(this, "data/test.tmx"));
		establishConnection();
		
	}
	
	private void establishConnection() {
		Socket clientSocket = Gdx.net.newClientSocket(Protocol.TCP, "localhost", 1337, null);
		outputStream = clientSocket.getOutputStream();
		inputStream = clientSocket.getInputStream();
	}

	public void startMap(String mapName) {
		setScreen(new DefaultLevelScreen(this, mapName));
	}

	@Override
	public void render() {
		super.render();
		// output the current FPS
		fpsLogger.log();
	}

	public Screen getMainMenuScreen() {
		return mainMenuScreen;
	}
	
	public void writeToServer(byte[] b){
		try {
			if(outputStream != null){
				outputStream.write(b);
				outputStream.write(13); // \n
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
