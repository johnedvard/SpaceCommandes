package com.rauma.lille.network;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

public class KillDeathRatioCommand extends Command {

	private HashMap<String, Vector2> kdratios;

	public KillDeathRatioCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public KillDeathRatioCommand(HashMap<String, Vector2> kdratios) {
		this.kdratios = kdratios;
	}

	public HashMap<String, Vector2> getKdratios() {
		return kdratios;
	}
}
