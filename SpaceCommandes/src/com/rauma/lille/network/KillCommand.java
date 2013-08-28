package com.rauma.lille.network;

public class KillCommand extends Command{

	
	private int playerId;
	private int idOfKiller;
	public KillCommand() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param playerId Id of the dead player
	 * @param playerThatGotTheLastHit Id of the player that killed the player with the id: playerId
	 */
	public KillCommand(int playerId, int playerThatGotTheLastHit){
		this.playerId = playerId;
		this.idOfKiller = playerThatGotTheLastHit;
		
	}
	public int getPlayerId() {
		return playerId;
	}
	public int getIdOfKiller(){
		return idOfKiller;
	}
}
