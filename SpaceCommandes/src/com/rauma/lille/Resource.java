package com.rauma.lille;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public final class Resource {

	public static Texture bgTexture;
	public static Texture uiTexture;
	public static Texture colorTexture;
	public static Texture bubbleTexture1, bubbleTexture2;
	public static Texture ballTexture;
	public static Texture boxTexture;

	public static Music bgMusic;
	public static Sound actorSound1;

	public static void initalize() {
		bgTexture = new Texture(Gdx.files.internal("data/stones.jpg"));
		uiTexture = new Texture(Gdx.files.internal("data/ui.png"));
		colorTexture = new Texture(Gdx.files.internal("data/Textures.png"));
		bubbleTexture1 = new Texture(Gdx.files.internal("data/star.png"));
		bubbleTexture2 = new Texture(Gdx.files.internal("data/ball.png"));
		ballTexture = new Texture(Gdx.files.internal("data/ball-64.png"));
		boxTexture = new Texture(Gdx.files.internal("data/box-64.png"));
	}

	protected void finalize() {
		bgTexture.dispose();
		uiTexture.dispose();
		colorTexture.dispose();
		bubbleTexture1.dispose();
		bubbleTexture2.dispose();
		ballTexture.dispose();
		boxTexture.dispose();
	}
}