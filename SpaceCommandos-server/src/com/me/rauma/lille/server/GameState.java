package com.me.rauma.lille.server;

import java.util.List;

import com.rauma.lille.network.CommandStartGame;

public class GameState {

	
	
	public GameState(List<SpaceClientConnection> clientsToPlayTogether) {
		
		for(SpaceClientConnection scc : clientsToPlayTogether){
			System.out.println("sending start game info to client: ");
			scc.sendMessage(new CommandStartGame(123));
		}
	}

}
