package com.rauma.lille;

import com.rauma.lille.actors.Bullet;

/**
 * @author frank
 *
 */
public class BulletContactHandler {

	public void handleBulletBeginContact(Bullet bullet, Object other) {
		if (bullet != null) {
			bullet.beginContact(other);
		}
	}

	public void handleBulletEndContact(Bullet bullet, Object other) {
		if (bullet != null) {
			bullet.endContact(other);
		}
	}
}