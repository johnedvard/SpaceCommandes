package com.rauma.lille.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.rauma.lille.SpaceGame;

public class SplashScreen extends AbstractScreen {
	private Texture splashTexture;
	private TextureRegion splashTextureRegion;
	private Rectangle splashScreenRectangle;

	public SplashScreen(SpaceGame game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		// load the splash image and create the texture region
		splashTexture = new Texture(Gdx.files.internal("data/libgdx.png"));

		// we set the linear texture filter to improve the stretching
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// in the image atlas, our splash image begins at (0,0) at the
		// upper-left corner and has a dimension of 512x301
		splashTextureRegion = new TextureRegion(splashTexture, 0, 0, 512, 320);
		splashScreenRectangle = new Rectangle(0,0,Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		batch.begin();
		batch.draw(splashTextureRegion, splashScreenRectangle.x,splashScreenRectangle.y);
		batch.end();
		
		// go to main menu
		if (Gdx.input.justTouched())
            game.setScreen(game.getMainMenuScreen());
	}

	@Override
	public void dispose() {
		super.dispose();
		splashTexture.dispose();
	}
}