package com.rauma.lille;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rauma.lille.actors.Bullet;

public class BulletContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();

		Object userDataA = bodyA.getUserData();
		Object userDataB = bodyB.getUserData();
		
		Bullet bullet = null;
		if (userDataA instanceof Bullet) {
			bullet = (Bullet) userDataA;
		} else if (userDataB instanceof Bullet) {
			bullet = (Bullet) userDataB;
		}
		if (bullet != null) {
			BulletFactory.release(bullet);
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
