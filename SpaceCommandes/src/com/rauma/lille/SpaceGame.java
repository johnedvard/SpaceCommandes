package com.rauma.lille;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Json;
import com.rauma.lille.network.ApplyDamageCommand;
import com.rauma.lille.network.Command;
import com.rauma.lille.network.KillCommand;
import com.rauma.lille.network.KillDeathRatioCommand;
import com.rauma.lille.network.PlayerAimedCommand;
import com.rauma.lille.network.PositionCommand;
import com.rauma.lille.network.StartGameCommand;
import com.rauma.lille.network.SpaceServerConnection;
import com.rauma.lille.screens.DefaultLevelScreen;

public class SpaceGame extends Game {
	private FPSLogger fpsLogger;
	private Screen splashScreen;
	private Screen mainMenuScreen;

	public static final float VIRTUAL_WIDTH = 800;
	public static final float VIRTUAL_HEIGHT = 480;
	public static final float ASPECT_RATIO = VIRTUAL_WIDTH/VIRTUAL_HEIGHT;

	public static final float WORLD_SCALE = 100;
	public static final Vector2 WORLD_GRAVITY = new Vector2(0f, -10f);

	private SpaceServerConnection client;
	private Json json = new Json();
	private DefaultLevelScreen defaultLevelScreen;

	@Override
	public void create() {
		fpsLogger = new FPSLogger();
		
		Texture.setEnforcePotImages(false);
		Resource.initalize();
		
		defaultLevelScreen = new DefaultLevelScreen(this, "data/test.tmx");
		setScreen(defaultLevelScreen);
		establishConnection();
		
	}
	
	private void establishConnection() {
		String host = "localhost";
//		String host = "10.254.9.140";
		int port = 1337;
		Socket socket = Gdx.net.newClientSocket(Protocol.TCP, host, port, null);
		client = new SpaceServerConnection(socket, this);
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
	
	public void writeToServer(Command command){
		if(client != null){
			byte[] b;
			b = (json.toJson(command,Command.class)+ "\n").getBytes();
			client.writeToServer(b);
		}
	}

	public void createNewGame(StartGameCommand startGameCommand) {
		defaultLevelScreen.createNewGame(startGameCommand);
	}

	public void updatePlayerPos(PositionCommand commandPos) {
		defaultLevelScreen.updatePlayerPos(commandPos);
	}

	public void playerAimedCommand(PlayerAimedCommand playerAimedCommand) {
		defaultLevelScreen.playerAimedCommand(playerAimedCommand);
	}

	public void applyDamageCommand(ApplyDamageCommand applyDmgCommand) {
		defaultLevelScreen.applyDamageCommand(applyDmgCommand);
	}

	public void killCommand(KillCommand killCommand) {
		defaultLevelScreen.killCommand(killCommand);
		
	}

	public void killDeathRationCommand(KillDeathRatioCommand kdratioCommmad) {
		defaultLevelScreen.killDeathRationCommand(kdratioCommmad);
		
	}
}
