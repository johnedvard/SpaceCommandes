package com.rauma.lille.network;

public class ApplyDamageCommand extends Command{
	private int id;
	private float damage;

	public ApplyDamageCommand() {
	}
	
	public ApplyDamageCommand(int id, float damage) {
		this.id = id;
		this.damage = damage;
	}

	public int getId() {
		return id;
	}

	public float getDamage() {
		return damage;
	}
	
}
