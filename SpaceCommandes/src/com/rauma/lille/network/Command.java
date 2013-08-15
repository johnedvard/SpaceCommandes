package com.rauma.lille.network;

/**
 * Only commands are allowed to send over the network.
 * We need to create extra constructors for extra commands we want to send as
 * well as an extra type, like POSITION
 * 
 * @author johnedvard
 *
 */
public class Command {
	public static final int POSITION = 0;
	public static final int START_GAME = 1;
	protected int type = -1;
	protected float x= -1;
	protected float y= -1;
	
	protected Command(){
	}
	
	@Override
	public String toString() {
		return "type: " + type;
	}
}
