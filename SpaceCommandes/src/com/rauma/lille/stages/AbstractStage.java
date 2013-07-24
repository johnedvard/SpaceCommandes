package com.rauma.lille.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class AbstractStage extends Stage {
	
	public AbstractStage(float width, float height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		setCamera(new OrthographicCamera(width, height));    
		getCamera().position.set( width / 2, height / 2, 0 );

	}
}
