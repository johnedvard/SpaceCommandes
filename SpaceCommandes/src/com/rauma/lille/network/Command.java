package com.rauma.lille.network;

import com.badlogic.gdx.utils.Json;
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
	private Json json;
	private Object command;
	
	private Command(Object command){
		json = new Json();
		this.command= command;
	}
	public Command(CommandPosition position) {
		this((Object)position);
	}
	
	@Override
	public String toString() {
		return json.toJson(command);
	}
}
