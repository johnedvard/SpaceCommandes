package com.me.rauma.lille.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.rauma.lille.network.ApplyDamageCommand;
import com.rauma.lille.network.Command;
import com.rauma.lille.network.KillCommand;
import com.rauma.lille.network.KillDeathRatioCommand;
import com.rauma.lille.network.PlayerAimedCommand;
import com.rauma.lille.network.PositionCommand;
import com.rauma.lille.network.StartGameCommand;

public class Game {
	private Json json = new Json();
	private List<SpaceClientConnection> clientsInGame = new ArrayList<SpaceClientConnection>();
	// the playerId as the key, the x in the vector is the kills, and the y is the deaths. 
	private HashMap<String,Vector2> playerKDRatios = new HashMap<String,Vector2>(); 
	
	public Game(List<SpaceClientConnection> clientsToPlayTogether) {
		int startPos = 10;
		int numPlayers= clientsToPlayTogether.size();
		int yourId = 1;
		CommandMessageListener listener = new CommandMessageListener();
		for(SpaceClientConnection scc : clientsToPlayTogether){
			System.out.println("sending start game info to client: ");
			scc.setId(yourId);
			scc.addCommandMessageListener(listener);
			scc.sendMessage(new StartGameCommand(yourId));
			playerKDRatios.put(""+yourId, new Vector2(0,0));
			scc.sendMessage(new StartGameCommand(yourId,numPlayers,"sp"+(++yourId)));
			clientsInGame.add(scc);
			yourId++;
		}
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
					int id = deathCommand.getPlayerId();
					int killedById = deathCommand.getGotKilledByPlayerId();
					System.out.println("player: " + id + "was killed by player: " + killedById);
					// add a death count to the player who died.
					Vector2 prevStatsPlayerId = playerKDRatios.get(""+id);
					prevStatsPlayerId.y++;
					// add a kill count to the player who killed the player,
					Vector2 prevStatskilledById = playerKDRatios.get(""+killedById);
					prevStatskilledById.x++;
					System.out.println("ratios: "+ playerKDRatios);
					System.out.println("stats dead plaeyr: " + playerKDRatios.get(""+id));
					System.out.println("stats killer plaeyr: " + playerKDRatios.get(""+killedById));
					KillDeathRatioCommand kdratioCommand = new KillDeathRatioCommand(playerKDRatios);
					for(SpaceClientConnection scc : clientsInGame){
						scc.sendMessage(deathCommand);
						scc.sendMessage(kdratioCommand);
					}
				} 
				else {
					System.out.println("got something else: " + string);
				}
			}
		}
	}
	
}
