package com.rauma.lille.network;

public class KillCommand extends Command{

	
	private int playerId;
	public KillCommand() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Id of the dead player
	 * @param playerId
	 */
	public KillCommand(int playerId){
		this.playerId = playerId;
		
	}
	public int getPlayerId() {
		return playerId;
	}
}
