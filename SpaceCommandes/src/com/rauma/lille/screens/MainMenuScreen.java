package com.rauma.lille.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rauma.lille.MyStage;
import com.rauma.lille.SpaceGame;

public class MainMenuScreen extends AbstractScreen {
	private Stage stage;
	
	 // setup the dimensions of the menu buttons
    private static final float BUTTON_WIDTH = 100f;
    private static final float BUTTON_HEIGHT = 30f;
    private static final float BUTTON_SPACING = 10f;
	private TextButton btnNewGame;

	private Texture splashTexture;

	private Label lblWelcome;

	private Touchpad touchpad;
    
	public MainMenuScreen(final SpaceGame game) {
		super(game);
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		stage = new MyStage(width,height,false);
		Gdx.input.setInputProcessor(stage);
		
		//create the welcome label
        BitmapFont defaultFont = new BitmapFont();
        LabelStyle style = new LabelStyle(defaultFont,Color.WHITE); 
		lblWelcome = new Label( "Welcome to Space Commandes!",style);
		lblWelcome.setX((stage.getWidth() - lblWelcome.getWidth())/2);
		lblWelcome.setY((stage.getHeight()/2)+100);
		stage.addActor(lblWelcome);
		
		// create the button
		splashTexture = new Texture(Gdx.files.internal("data/libgdx.png"));
		TextureRegion splashTextureRegionUp = new TextureRegion(splashTexture, 320, 320, 321, 321);
		TextureRegion splashTextureRegionDown = new TextureRegion(splashTexture, 50, 50, 0, 0);
		NinePatch ninePatchUp = new NinePatch(splashTextureRegionUp);
		NinePatch ninePatchDown = new NinePatch(splashTextureRegionDown);
		
		Drawable drawableUp = new NinePatchDrawable(ninePatchUp);
		Drawable drawableDown = new NinePatchDrawable(ninePatchDown);
		TextButtonStyle buttonStyle = new TextButtonStyle(drawableUp,drawableDown,drawableUp,defaultFont);
		btnNewGame = new TextButton("New Game",buttonStyle);
		btnNewGame.setWidth(BUTTON_WIDTH);
		btnNewGame.setHeight(BUTTON_HEIGHT);
		btnNewGame.setX((stage.getWidth() - btnNewGame.getWidth())/2);
		btnNewGame.setY((stage.getHeight()/2)-100);
		btnNewGame.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				// change the screen to the game screen.
				System.out.println("clicked x,y: " + x +","+ y);
				game.setScreen(game.getGameScreen());
			}
		});
		stage.addActor(btnNewGame);
		
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		stage.draw();
		
	}
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		System.out.println("width, height" + width +","+ height);
		stage.setViewport(width, height, false);
		
		btnNewGame.setX((stage.getWidth() - btnNewGame.getWidth())/2);
		btnNewGame.setY((stage.getHeight()/2)-100);
		
		lblWelcome.setX((stage.getWidth() - lblWelcome.getWidth())/2);
		lblWelcome.setY((stage.getHeight()/2)+100);
	}

	@Override
	public void dispose() {
		super.dispose();
		splashTexture.dispose();
	}
}
