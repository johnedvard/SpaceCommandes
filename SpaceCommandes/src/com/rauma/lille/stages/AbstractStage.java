package com.rauma.lille.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class AbstractStage extends Stage {
	
	public AbstractStage(float width, float height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
	}
}
