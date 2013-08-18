package com.rauma.lille.network;


public class CommandStartGame extends Command{
	
	protected int playerId = -1;  // used in CommandStartGame
	
	private CommandStartGame() {
	}
	
	public CommandStartGame(int playerId) {
		this.playerId = playerId;
	}
	
	public int getPlayerId() {
		return playerId;
	}

}
