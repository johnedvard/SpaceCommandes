package com.rauma.lille.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * This class is responsible for drawing the background. I think there should be
 * no or very little user interaction on this stage. There should be no physics here.
 * 
 * @author frank
 * 
 */
public class BackgroundStage extends Stage {

	public BackgroundStage(float width, float height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
	}
	
	@Override
	public void draw() {
		super.draw();
		// TODO(frank); disable blending for background stage
	}
}
