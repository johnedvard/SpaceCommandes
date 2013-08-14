package com.rauma.lille.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rauma.lille.SpaceGame;

public class MainMenuScreen extends DefaultScreen {
	private Stage stage = new Stage();
	
	 // setup the dimensions of the menu buttons
    private static final float BUTTON_WIDTH = 100f;
    private static final float BUTTON_HEIGHT = 30f;
    private static final float BUTTON_SPACING = 10f;

	private Texture splashTexture;

	private Label lblWelcome;

	private TextButton levelOneBtn;

	private TextButton levelTwoBtn;

    
	public MainMenuScreen(final SpaceGame game) {
		super(game);
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

		levelOneBtn = new TextButton("Level 1", buttonStyle);
		levelOneBtn.setWidth(BUTTON_WIDTH);
		levelOneBtn.setHeight(BUTTON_HEIGHT);
		levelOneBtn.setX((stage.getWidth() - levelOneBtn.getWidth())/2);
		levelOneBtn.setY((stage.getHeight()/2));
		levelOneBtn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				// change the screen to the game screen.
				System.out.println("clicked x,y: " + x +","+ y);
				game.startMap("data/test.tmx");
			}
		});
		stage.addActor(levelOneBtn);

		levelTwoBtn = new TextButton("Level 2", buttonStyle);
		levelTwoBtn.setWidth(BUTTON_WIDTH);
		levelTwoBtn.setHeight(BUTTON_HEIGHT);
		levelTwoBtn.setX((stage.getWidth() - levelTwoBtn.getWidth())/2);
		levelTwoBtn.setY((stage.getHeight()/2)-BUTTON_HEIGHT-BUTTON_SPACING);
		levelTwoBtn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				// change the screen to the game screen.
				System.out.println("clicked x,y: " + x +","+ y);
				game.startMap("data/myFirstMap.tmx");
			}
		});
		stage.addActor(levelTwoBtn);
		
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
		
		levelOneBtn.setX((stage.getWidth() - levelOneBtn.getWidth())/2);
		levelOneBtn.setY((stage.getHeight()/2)-BUTTON_HEIGHT);
		
		levelTwoBtn.setX((stage.getWidth() - levelTwoBtn.getWidth())/2);
		levelTwoBtn.setY((stage.getHeight()/2)-BUTTON_HEIGHT-BUTTON_SPACING);
		
		lblWelcome.setX((stage.getWidth() - lblWelcome.getWidth())/2);
		lblWelcome.setY((stage.getHeight()/2)+100);
	}

	@Override
	public void dispose() {
		super.dispose();
		splashTexture.dispose();
	}
}
