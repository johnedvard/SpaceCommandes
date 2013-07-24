package com.rauma.lille.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * This class is responsible for drawing the background. I think there should be
 * no or very little user interaction on this stage. There should be no physics here.
 * 
 * @author frank
 * 
 */
public class BackgroundStage extends Stage {

	public BackgroundStage(int width, int height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
	}
	@Override
	public void draw() {
		super.draw();
		 // the following code clears the screen with the given RGB color (green)
        Gdx.gl.glClearColor( 0.5f, 0.8f, 0.5f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
	}
	
}
