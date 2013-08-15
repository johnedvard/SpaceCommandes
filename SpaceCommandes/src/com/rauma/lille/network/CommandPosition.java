package com.rauma.lille.network;

public class CommandPosition extends Command {

	public CommandPosition(float x, float y) {
		this();
		type = Command.POSITION;
		this.x = x;
		this.y = y;
	}
	
	private CommandPosition(){
		super();
	}
	
	
}
