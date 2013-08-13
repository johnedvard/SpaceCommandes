package com.rauma.lille.network;

public class CommandPosition {

	private int type = Command.POSITION;
	private String playerId;
	private float x;
	private float y;
	public CommandPosition(String playerId, float x, float y) {
		this.playerId = playerId;
		this.x = x;
		this.y = y;
	}
}
