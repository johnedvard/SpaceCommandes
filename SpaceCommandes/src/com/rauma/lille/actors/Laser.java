package com.rauma.lille.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Laser extends Actor {

	private int lifeTimeOfLaser = 0;
	private int totalTimeOfLaser = 5;

	public Laser() {
	}

	public void reset() {
		lifeTimeOfLaser = 0;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		lifeTimeOfLaser += delta;
	}

	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		super.draw(spriteBatch, parentAlpha);
		spriteBatch.end(); // actual drawing is done on end(); if we do not end,
							// we contaminate previous rendering.
		spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		spriteBatch.begin();
		Color color = Color.ORANGE;
		spriteBatch.setColor(color);
		color.a = 1 - (lifeTimeOfLaser / totalTimeOfLaser) ^ 2;

		TextureAtlas atlas = new TextureAtlas(
				Gdx.files.internal("data/laser.txt"));
		Sprite laserStart = atlas.createSprite("laserStart");
		Sprite laserStartOverlay = atlas.createSprite("laserStartOverlay");
		float[] vertices = new float[0];
		AtlasRegion region = atlas.findRegion("laser");
		int scale = 5;
		int x = 10;
		int y = 10;
		int originX = 0;
		int originY = 0;
		int width = 20;
		int height = 20;
		int scaleX = 1;
		int scaleY = 1;
		int rotation = 0;
		float[] constructEndingMesh = constructEndingMesh(vertices, region, scale, x, y, originX, originY, width, height, scaleX, scaleY, rotation, color, color.a);

		// TODO(frank): now what....
		
		spriteBatch.end();
		spriteBatch.begin(); // required?
	}

	/**
	 * Creates a mesh which will draw a repeated texture. To be used whenever we
	 * are dealing with a region of a TextureAtlas
	 * 
	 * @param vertices
	 *            For re-use, the vertices to use for the mesh. If insufficient
	 *            are provided, a new array will be constructed
	 * @param region
	 *            The AtlasRegion to use
	 * @param scale
	 *            The factor by which we want to repeat our texture
	 * @param x
	 * @param y
	 * @param originX
	 * @param originY
	 * @param width
	 * @param height
	 * @param scaleX
	 *            Scale by which we want to expand the mesh on X
	 * @param scaleY
	 *            Scale by which we want to expand the mesh on Y
	 * @param rotation
	 *            Degrees of rotation for mesh
	 * @param colorBase
	 *            The initial base color
	 * @param alpha
	 *            The alpha by which to mult the colorBase by; resulting in the
	 *            end interpolation target.
	 * @return
	 */
	private static final float[] constructEndingMesh(float[] vertices,
			AtlasRegion region, int scale, float x, float y, float originX,
			float originY, float width, float height, float scaleX,
			float scaleY, float rotation, Color colorT, float alpha) {
		if (scale * 20 > vertices.length)
			vertices = new float[20 * scale];

		float color = colorT.toFloatBits();
		float colorE;

		int idx = 0;

		// bottom left and top right corner points relative to origin
		final float worldOriginX = x + originX;
		final float worldOriginY = y + originY;
		float fx = -originX;
		float fy = -originY;
		float fx2 = width - originX;
		float fy2 = height - originY;

		// scale
		if (scaleX != 1 || scaleY != 1) {
			fx *= scaleX;
			fy *= scaleY;
			fx2 *= scaleX;
			fy2 *= scaleY;
		}

		// construct corner points, start from top left and go counter clockwise
		final float p1x = fx;
		final float p1y = fy;
		final float p2x = fx;
		final float p2y = fy2;
		final float p3x = fx2;
		final float p3y = fy2;
		final float p4x = fx2;
		final float p4y = fy;

		float Fx1;
		float Fy1;
		float Fx2;
		float Fy2;
		float Fx3;
		float Fy3;
		float Fx4;
		float Fy4;

		// rotate
		if (rotation != 0) {
			final float cos = MathUtils.cosDeg(rotation);
			final float sin = MathUtils.sinDeg(rotation);

			Fx1 = cos * p1x - sin * p1y;
			Fy1 = sin * p1x + cos * p1y;

			Fx2 = cos * p2x - sin * p2y;
			Fy2 = sin * p2x + cos * p2y;

			Fx3 = cos * p3x - sin * p3y;
			Fy3 = sin * p3x + cos * p3y;

			Fx4 = Fx1 + (Fx3 - Fx2);
			Fy4 = Fy3 - (Fy2 - Fy1);
		} else {
			Fx1 = p1x;
			Fy1 = p1y;

			Fx2 = p2x;
			Fy2 = p2y;

			Fx3 = p3x;
			Fy3 = p3y;

			Fx4 = p4x;
			Fy4 = p4y;
		}

		float x1 = Fx1 + worldOriginX;
		float y1 = Fy1 + worldOriginY;
		float x2 = Fx2 + worldOriginX;
		float y2 = Fy2 + worldOriginY;

		float scaleX2 = ((Fx2 - Fx1) / scale);
		float scaleY2 = ((Fy2 - Fy1) / scale);
		float scaleX3 = ((Fx3 - Fx4) / scale);
		float scaleY3 = ((Fy3 - Fy4) / scale);
		float scaleAlpha = (colorT.a - (colorT.a * alpha)) / scale;

		float x3 = x1;
		float y3 = y1;
		float x4 = x2;
		float y4 = y2;

		final float u = region.getU();
		final float v = region.getV();
		final float u2 = region.getU2();
		final float v2 = region.getV2();

		for (int i = 1; i <= scale; i++) {
			x1 = Fx1 + scaleX2 * (i - 1) + worldOriginX;
			y1 = Fy1 + scaleY2 * (i - 1) + worldOriginY;
			x2 = Fx1 + scaleX2 * i + worldOriginX;
			y2 = Fy1 + scaleY2 * i + worldOriginY;

			x3 = Fx4 + scaleX3 * i + worldOriginX;
			y3 = Fy4 + scaleY3 * i + worldOriginY;
			x4 = Fx4 + scaleX3 * (i - 1) + worldOriginX;
			y4 = Fy4 + scaleY3 * (i - 1) + worldOriginY;

			color = colorT.toFloatBits();
			colorT.a -= scaleAlpha;
			colorE = colorT.toFloatBits();

			vertices[idx++] = x1;
			vertices[idx++] = y1;
			vertices[idx++] = color;
			vertices[idx++] = u;
			vertices[idx++] = v;

			vertices[idx++] = x2;
			vertices[idx++] = y2;
			vertices[idx++] = colorE;
			vertices[idx++] = u;
			vertices[idx++] = v2;

			vertices[idx++] = x3;
			vertices[idx++] = y3;
			vertices[idx++] = colorE;
			vertices[idx++] = u2;
			vertices[idx++] = v2;

			vertices[idx++] = x4;
			vertices[idx++] = y4;
			vertices[idx++] = color;
			vertices[idx++] = u2;
			vertices[idx++] = v;

		}

		return vertices;
	}
}
