package com.rauma.lille.network;

public class KillCommand extends Command{

	
	// the player who got killed
	private int playerId;
	private int gotKilledByPlayerId;
	public KillCommand() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Id of the dead player
	 * @param playerId
	 */
	public KillCommand(int playerId, int gotKilledByPlayerId){
		this.playerId = playerId;
		this.gotKilledByPlayerId = gotKilledByPlayerId;
		
	}
	public int getPlayerId() {
		return playerId;
	}
	public int getGotKilledByPlayerId() {
		return gotKilledByPlayerId;
	}
}
