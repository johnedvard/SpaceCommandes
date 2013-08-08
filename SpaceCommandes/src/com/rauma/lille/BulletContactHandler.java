package com.rauma.lille;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.rauma.lille.actors.Bullet;

/**
 * @author frank
 *
 */
public class BulletContactHandler {

	public void handleBulletBeginContact(Bullet bullet, Object other) {
		if (bullet != null) {
			String name2 = other==null?"unknown":other.toString();
			if (other instanceof Actor) {
				Actor actor = (Actor) other;
				name2 = actor.getName();
			}
			System.out.println(bullet + " started contact with " + name2);

			bullet.beginContact(other);
		}
	}
	public void handleBulletEndContact(Bullet bullet, Object other) {

		if (bullet != null) {
			String name2 = other==null?"unknown":other.toString();
			if (other instanceof Actor) {
				Actor actor = (Actor) other;
				name2 = actor.getName();
			}
			System.out.println(bullet + " ended contact with " + name2);

			bullet.endContact(other);
		}
	
	}
}