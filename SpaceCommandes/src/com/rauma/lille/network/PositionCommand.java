package com.rauma.lille.network;

public class PositionCommand extends Command {
	
	private float x= -1;
	private float y= -1;
	private int id = -1; //player id
	
	private PositionCommand() {
	}
	
	public PositionCommand(int id, float x, float y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public int getId() {
		return id;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
}
