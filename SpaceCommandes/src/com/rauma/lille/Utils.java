package com.rauma.lille;

import com.badlogic.gdx.math.Vector2;

public final class Utils {

	public static Vector2 Screen2World(final Vector2 vct) {
		return new Vector2(vct.x / SpaceGame.WORLD_SCALE, vct.y
				/ SpaceGame.WORLD_SCALE);
	}

	public static Vector2 Screen2World(final float x, final float y) {
		return new Vector2(x / SpaceGame.WORLD_SCALE, y / SpaceGame.WORLD_SCALE);
	}

	public static float Screen2World(final float v) {
		return v / SpaceGame.WORLD_SCALE;
	}

	public static Vector2 getWorldBoxCenter(final Vector2 top,
			final Vector2 size) {
		return new Vector2(top.x + size.x / 2, top.y + size.y / 2);
	}

	public static Vector2 Frame2Screen(final int x, final int y) {
		return new Vector2(x, SpaceGame.SCREEN_HEIGHT - y);
	}

//	public static Vector2[] Screen2World(float[] vertices) {
//		Vector2[] vector2s = new Vector2[vertices.length / 2];
//		for (int i = 0; i < vector2s.length; i++) {
//			vector2s[i].x = vertices[2 * i] / SpaceGame.WORLD_SCALE;
//			vector2s[i].y = vertices[2 * i + 1] / SpaceGame.WORLD_SCALE;
//		}
//		return vector2s;
//	}

	public static float[] Screen2World(float[] vertices) {
		float[] vector2s = new float[vertices.length];
		for (int j = 0; j < vector2s.length; j++) {
			vector2s[j] = vertices[j] / SpaceGame.WORLD_SCALE;
		}
		return vector2s;
	}
	
	/**
	 * return a vector from the angle (in radians), multiplied with the given magnitude
	 * @param angleRad
	 * @param magnitude
	 * @return
	 */
	public static Vector2 getVector(float angleRad, float magnitude) {
		Vector2 v = new Vector2((float) Math.sin(angleRad), (float) Math.cos(angleRad));
		if(magnitude != 1)
			v = v.scl(magnitude);
		return v;
	}


}
