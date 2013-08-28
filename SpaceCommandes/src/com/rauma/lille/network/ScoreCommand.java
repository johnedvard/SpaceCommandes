package com.rauma.lille.network;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

public class ScoreCommand extends Command{
	
	private HashMap<String, Vector2> killDeathRatios;

	public ScoreCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public ScoreCommand(HashMap<String,Vector2> killDeathRatios){
		this.killDeathRatios = killDeathRatios;
	}
	
	public Vector2 getKillDeathRatio(int playerId){
		return killDeathRatios.get(""+playerId);
	}
	public HashMap<String,Vector2> getKillDeathRatios(){
		return killDeathRatios;
	}
}
