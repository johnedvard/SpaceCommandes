package com.rauma.lille.network;

public class PositionCommand extends Command {
	
	private float x= -1;
	private float y= -1;
	private int id = -1; //player id
	private float angle = -1;
	private PositionCommand() {
	}
	
	public PositionCommand(int id, float x, float y, float angle) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.angle = angle;
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

	public float getAngle() {
		return angle;
	}
}
