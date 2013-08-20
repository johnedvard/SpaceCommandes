package com.rauma.lille.network;

import java.util.Map;


public class StartGameCommand extends Command{
	
	private int playerId = -1;  // used in CommandStartGame
	private int numPlayers = -1;
	private String spawnPointName;
	private StartGameCommand() {
	}
	
	public StartGameCommand(int playerId, int numPlayers, String spawnPointName) {
		this.playerId = playerId;
		this.spawnPointName = spawnPointName;
		this.numPlayers = numPlayers;
		
	}
	
	public int getPlayerId() {
		return playerId;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public String getSpawnPointName() {
		return spawnPointName;
	}

}
