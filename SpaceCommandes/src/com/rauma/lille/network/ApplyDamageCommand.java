package com.rauma.lille.network;

public class ApplyDamageCommand extends Command{
	private int id;
	private float damage;
	private int firedByOtherPlayer;

	public ApplyDamageCommand() {
	}
	
	/**
	 * 
	 * @param id
	 * @param damage
	 * @param firedByOtherPlayer the playerId that shot the bullet hitting the player with the specific id 
	 */
	public ApplyDamageCommand(int id, float damage, int firedByOtherPlayer) {
		this.id = id;
		this.damage = damage;
		this.firedByOtherPlayer = firedByOtherPlayer;
	}

	public int getId() {
		return id;
	}

	public float getDamage() {
		return damage;
	}
	public int getFiredByOtherPlayerId(){
		return firedByOtherPlayer;
	}
}
