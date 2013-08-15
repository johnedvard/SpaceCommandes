package com.rauma.lille.network;

public class CommandStartGame extends Command{
	
	private CommandStartGame() {
		super();
	}

	public CommandStartGame(int x){
		this();
		type = Command.START_GAME;
	}
}
