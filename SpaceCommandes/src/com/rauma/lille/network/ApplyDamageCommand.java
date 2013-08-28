package com.rauma.lille.network;

public class ApplyDamageCommand extends Command{
	private int id;
	private float damage;
	private int playerWhoDealtDamageId;

	public ApplyDamageCommand() {
	}
	
	public ApplyDamageCommand(int id, float damage, int playerWhoDealtDamageId) {
		this.id = id;
		this.damage = damage;
		this.playerWhoDealtDamageId = playerWhoDealtDamageId;
	}

	public int getId() {
		return id;
	}

	public float getDamage() {
		return damage;
	}
	public int getPlayerWhoDealtDamageId(){
		return playerWhoDealtDamageId;
	}
	
}
