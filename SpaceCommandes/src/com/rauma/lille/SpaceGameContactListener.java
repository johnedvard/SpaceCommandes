package com.rauma.lille;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rauma.lille.actors.Bullet;

public class SpaceGameContactListener implements ContactListener {
	private BulletContactHandler bch = new BulletContactHandler();
	
	@Override
	public void beginContact(Contact contact) {
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();

		Object userDataA = bodyA.getUserData();
		Object userDataB = bodyB.getUserData();
		
		// Handle begin bullet impact
		if (userDataA instanceof Bullet) {
			bch.handleBulletBeginContact((Bullet) userDataA, userDataB);
		} else if (userDataB instanceof Bullet) {
			bch.handleBulletBeginContact((Bullet) userDataB, userDataA);
		}
		
		
		
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		Body bodyA = null;
		Body bodyB = null;
		if(fixtureA != null)
			bodyA = fixtureA.getBody();
		if(fixtureB != null)
			bodyB = fixtureB.getBody();

		Object userDataA = null;
		Object userDataB = null;
		
		if(bodyA != null)
			userDataA = bodyA.getUserData();
		if(bodyB != null)
			userDataB = bodyB.getUserData();
		
		// Handle end bullet impact
		if (userDataA instanceof Bullet) {
			bch.handleBulletEndContact((Bullet) userDataA, userDataB);
		} else if (userDataB instanceof Bullet) {
			bch.handleBulletEndContact((Bullet) userDataB, userDataA);
		}
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
