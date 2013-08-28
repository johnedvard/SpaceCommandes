package com.me.rauma.lille.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.rauma.lille.network.ApplyDamageCommand;
import com.rauma.lille.network.EndGameCommand;
import com.rauma.lille.network.KillCommand;
import com.rauma.lille.network.PlayerAimedCommand;
import com.rauma.lille.network.PositionCommand;
import com.rauma.lille.network.ScoreCommand;
import com.rauma.lille.network.StartGameCommand;

public class Game {
	private Json json = new Json();
	private List<SpaceClientConnection> clientsInGame = new ArrayList<SpaceClientConnection>();
	// the String is the id of the player. The vector.x is the number of kills, and the vector.y is the number of deaths
	private HashMap<String, Vector2> killDeathRatios = new HashMap<String,Vector2>();
	private SpaceServer server;
	
	public Game(final List<SpaceClientConnection> clientsToPlayTogether, SpaceServer server) {
		this.server = server;
		int numPlayers= clientsToPlayTogether.size();
		int yourId = 0;
		CommandMessageListener listener = new CommandMessageListener();
		for(SpaceClientConnection scc : clientsToPlayTogether){
			System.out.println("sending start game info to client: " + yourId);
			killDeathRatios.put(""+yourId, new Vector2(0,0));
			scc.addCommandMessageListener(listener);
			scc.sendMessage(new StartGameCommand(yourId,numPlayers,"sp"+(++yourId)));
			clientsInGame.add(scc);
		}
		new Thread() {
			public void run() {
				int players = 0;
				do {
					players = 0;
					try {
						for (Iterator<SpaceClientConnection> it = clientsToPlayTogether.iterator(); it.hasNext();) {
							SpaceClientConnection spaceClientConnection = it.next();
							if(spaceClientConnection.isRunning()) {
								players++;
							}
						}
						sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} while(players > 1);
				
				endGame();
			};
		}.start();
	}
	
	protected void endGame() {
		System.out.println("ending game");
		for (SpaceClientConnection spaceClientConnection : clientsInGame) {
			spaceClientConnection.sendMessage(new EndGameCommand());
		}
		server.gameEnded(this);
	}
	
	public List<SpaceClientConnection> getClientsInGame() {
		return clientsInGame;
	}

	class CommandMessageListener {

		// send the message to all the clients if the message is a position message.
		public void sendMessage(String string) {
			if(string.startsWith("{")){ //FIXME (john) check if the message is a json message
				Object c = json.fromJson(null, string);
				if (c instanceof PositionCommand) { //
					PositionCommand commandPos = (PositionCommand) c;
					// send the position to all the clients
					for(SpaceClientConnection scc : clientsInGame){
						scc.sendMessage(commandPos);
					}
				}else if (c instanceof StartGameCommand) {
				}else if (c instanceof PlayerAimedCommand) {
					PlayerAimedCommand playerAimedCommand = (PlayerAimedCommand) c;
					// send the position to all the clients
					for(SpaceClientConnection scc : clientsInGame){
						scc.sendMessage(playerAimedCommand);
					}
				}else if (c instanceof ApplyDamageCommand) {
					ApplyDamageCommand applyDmgCommand = (ApplyDamageCommand) c;
					for(SpaceClientConnection scc : clientsInGame){
						scc.sendMessage(applyDmgCommand);
					}
				} else if (c instanceof KillCommand) {
					KillCommand deathCommand = (KillCommand) c;
					System.out.println("Got death command");
					int victim = deathCommand.getPlayerId();
					int killer = deathCommand.getIdOfKiller();
					System.out.println(killDeathRatios);
					Vector2 killerStats = killDeathRatios.get(""+killer);
					Vector2 victimStats = killDeathRatios.get(""+victim);
					killerStats.x++;
					victimStats.y++;
					killDeathRatios.put(""+victim, victimStats);
					killDeathRatios.put(""+killer, killerStats);
					ScoreCommand scoreCommand = new ScoreCommand(killDeathRatios);
					for(SpaceClientConnection scc : clientsInGame){
						scc.sendMessage(deathCommand);
						scc.sendMessage(scoreCommand);
					}
				} 
				else {
					System.out.println("got something else: " + string);
				}
			}
		}
	}
	
}
