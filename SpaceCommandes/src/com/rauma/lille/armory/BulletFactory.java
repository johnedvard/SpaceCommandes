package com.rauma.lille.armory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.physics.box2d.World;
import com.rauma.lille.actors.Bullet;

/**
 * @author frank
 *
 */
public class BulletFactory {
	private static final int BULLET_LIMIT = 50;
	private List<Bullet> freeBullets = Collections.synchronizedList(new ArrayList<Bullet>());
	private List<Bullet> busyBullets = Collections.synchronizedList(new ArrayList<Bullet>());
	
	private World world;
	private short categoryBits;
	private short maskBits;
	// the player who created the bulletFactory.
	private int playerId;

	public BulletFactory(short categoryBits, short maskBits, World world, int playerId) {
		this.categoryBits = categoryBits;
		this.maskBits = maskBits;
		this.world = world;
		this.playerId = playerId;
		create(BULLET_LIMIT);
	}

	public Bullet getBullet() {
		if (freeBullets.size() == 0) {
			if (busyBullets.size() < BULLET_LIMIT) {
				create(1);
			} else {
				return null;
			}
		}
		Bullet bullet = freeBullets.remove(0);
		if (bullet != null) {
			busyBullets.add(bullet);
		}

		return bullet;
	}

	private Bullet createBullet() {
		return new Bullet("Bullet " + categoryBits, categoryBits, maskBits, world, this, playerId);
	}

	/**
	 * Create bullet and add it to the free pool
	 * 
	 * @param numBullets
	 */
	private void create(int numBullets) {
		for (int j = 0; j < numBullets; j++) {
			freeBullets.add(createBullet());
		}
	}

	public void release(Bullet bullet) {
		busyBullets.remove(bullet);
		freeBullets.add(bullet);
		
//		System.out.println("free/busy: " + freeBullets.size() + "/" + busyBullets.size() + ": " + bullet);
	}
}
