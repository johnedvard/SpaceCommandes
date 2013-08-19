package com.rauma.lille.network;

public class PlayerAimedCommand extends Command {

	
	private float knobX = -1;
	private float knobY = -1;
	private float knobPercentY = -1;
	private float knobPercentX = -1;
	private int playerId = -1;
	
	private PlayerAimedCommand() {
	}
	public PlayerAimedCommand(int playerId, float knobX, float knobY, float knobPercentX, float knobPercentY){
		this.playerId  = playerId;
		this.knobX = knobX;
		this.knobY = knobY;
		this.knobPercentX = knobPercentX;
		this.knobPercentY = knobPercentY;
	}
	public float getKnobX() {
		return knobX;
	}
	public float getKnobY() {
		return knobY;
	}
	public float getKnobPercentY() {
		return knobPercentY;
	}
	public float getKnobPercentX() {
		return knobPercentX;
	}
	public int getPlayerId() {
		return playerId;
	}
}
