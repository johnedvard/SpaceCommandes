package com.rauma.lille.actors;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Shape extends Actor {
	protected ShapeRenderer renderer;
	protected World world;
	protected Fixture fixture;

	public Shape(World world) {
		renderer = new ShapeRenderer();
		this.world = world;
	}

	
	@Override
	public float getX() {
		return fixture.getBody().getPosition().x;
	}
	
	@Override
	public float getY() {
		return fixture.getBody().getPosition().y;
	}
}