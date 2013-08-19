package com.rauma.lille.network;


public class StartGameCommand extends Command{
	
	protected int playerId = -1;  // used in CommandStartGame
	
	private StartGameCommand() {
	}
	
	public StartGameCommand(int playerId) {
		this.playerId = playerId;
	}
	
	public int getPlayerId() {
		return playerId;
	}

}
