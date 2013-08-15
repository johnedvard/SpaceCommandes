package com.rauma.lille.network;

public class CommandPosition extends Command {

	private float x;
	private float y;
	public CommandPosition(float x, float y) {
		this();
		type = Command.POSITION;
		this.x = x;
		this.y = y;
	}
	
	public CommandPosition(){
		super();
	}
	
	
}
