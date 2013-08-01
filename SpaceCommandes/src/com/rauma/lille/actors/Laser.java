package com.rauma.lille.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Laser extends Actor {

	private float lifeTimeOfLaser = 2;
	private float totalTimeOfLaser = 1;
	private boolean active;
	private TextureRegion startBase;
	private TextureRegion startOverlay;
	private TextureRegion stretchableBase;
	private TextureRegion stretchableOverlay;
	private TextureRegion endBase;
	private TextureRegion endOverlay;
	private TextureRegion base;
	private TextureRegion overlay;

	public Laser() {
		Texture laserBase = new Texture(Gdx.files.internal("laser/laser_base.png"));
		Texture laserOverlay = new Texture(Gdx.files.internal("laser/laser_overlay.png"));

		base = new TextureRegion(laserBase, 0, 0, 64, 128);
		overlay = new TextureRegion(laserOverlay, 0, 0, 64, 128);
		
		endBase = new TextureRegion(laserBase, 0, 0, 64, 64);
		stretchableBase = new TextureRegion(laserBase, 0, 64, 64, 64);
		startBase = new TextureRegion(laserBase, 0, 128, 64, 64);

		endOverlay = new TextureRegion(laserOverlay, 0, 0, 64, 64);
		stretchableOverlay = new TextureRegion(laserOverlay, 0, 64, 64, 64);
		startOverlay = new TextureRegion(laserOverlay, 0, 128, 64, 64);
		
	}

	public void activate() {
		active = true;
		lifeTimeOfLaser = 0;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (!isActive())
			lifeTimeOfLaser += delta;
	}

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		super.draw(spriteBatch, parentAlpha);
		if (!isActive() && lifeTimeOfLaser > totalTimeOfLaser) {
			return;
		}

		spriteBatch.end(); // actual drawing is done on end(); if we do not end,
							// we contaminate previous rendering.
		spriteBatch.begin();
		spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		Color color = Color.GREEN;
		double i = (double) (lifeTimeOfLaser / totalTimeOfLaser);
		color.a = (float) (1.0f - Math.pow(i, 2));
		spriteBatch.setColor(color);
		
//		int scale = 2;
//		spriteBatch.draw(startBase, getX(), getY(), getX(), getY(), 64, 64, 1, 1, getRotation());
//		spriteBatch.draw(stretchableBase, getX(), getY()+64, getX(), getY(), 64, 64, 1, scale, getRotation());
//		spriteBatch.draw(endBase, getX(), getY()+64*scale, getX(), getY(), 64, 64, 1, 1, getRotation());
		
//		spriteBatch.draw(stretchableOverlay, getX(), getY(), getOriginX(), getOriginY(), 64, 64, 1, 2, getRotation());
		
		spriteBatch.draw(base, getX(), getY(), getOriginX(), getOriginY(), 64, 192, 1, 3, getRotation()-90);
		spriteBatch.draw(overlay, getX(), getY(), getOriginX(), getOriginY(), 64, 192, 1, 3, getRotation()-90);
		
		spriteBatch.end();
		spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch.begin(); // required?
	}

	public boolean isActive() {
		return active;
	}

	public void deactivate() {
		active = false;
	}
}
