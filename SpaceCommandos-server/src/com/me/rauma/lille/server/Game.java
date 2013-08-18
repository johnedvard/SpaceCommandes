package com.me.rauma.lille.server;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.rauma.lille.network.Command;
import com.rauma.lille.network.CommandPosition;
import com.rauma.lille.network.CommandStartGame;

public class Game {
	private Json json = new Json();
	private List<SpaceClientConnection> clientsInGame = new ArrayList<SpaceClientConnection>();
	public Game(List<SpaceClientConnection> clientsToPlayTogether) {
		int startPos = 10;
		int numPlayers= clientsToPlayTogether.size();
		int yourId = 1;
		CommandMessageListener listener = new CommandMessageListener();
		for(SpaceClientConnection scc : clientsToPlayTogether){
			System.out.println("sending start game info to client: ");
			scc.setId(yourId);
			scc.addCommandMessageListener(listener);
			scc.sendMessage(new CommandStartGame(yourId));
			clientsInGame.add(scc);
			yourId++;
		}
	}
	
	class CommandMessageListener {

		// send the message to all the clients if the message is a position message.
		public void sendMessage(String string) {
			if(string.startsWith("{")){ //FIXME (john) check if the message is a json message
				Object c = json.fromJson(null, string);
				if (c instanceof CommandPosition) { //
					CommandPosition commandPos = (CommandPosition) c;
					// send the position to all the clients
					for(SpaceClientConnection scc : clientsInGame){
						scc.sendMessage(commandPos);
					}
				}else if (c instanceof CommandStartGame) {
					
				}else {
					System.out.println("got something else: " + string);
				}
			}
		}
	}
	
}
