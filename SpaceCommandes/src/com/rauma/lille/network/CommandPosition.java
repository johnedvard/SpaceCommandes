package com.rauma.lille.network;

public class CommandPosition {

	private int type = Command.POSITION;
	private float x;
	private float y;
	public CommandPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
