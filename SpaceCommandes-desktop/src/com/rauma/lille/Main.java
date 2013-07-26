package com.rauma.lille;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.esotericsoftware.spine.Box2DExample;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "SpaceCommandes";
		cfg.useGL20 = false;
		cfg.width = (int) SpaceGame.SCREEN_WIDTH;
		cfg.height = (int) SpaceGame.SCREEN_HEIGHT;
		Box2DExample a = new Box2DExample();
		new LwjglApplication(a, cfg);
	}
}
