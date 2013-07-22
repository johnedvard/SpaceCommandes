package com.rauma.lille.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * This class is responsible for maintaining and drawing actors in the game, and
 * correlate them to the physics engine.
 * 
 * @author frank
 * 
 */
public class ActorStage extends Stage {

	public ActorStage(int width, int height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		
		// init world (box2d)
		// init actors (scene2s) with bodies (box2d)
		// translate events and such from world and bodies to actors in the scene
		// best/easiest example I've seen so far: https://code.google.com/p/codejie/source/browse/#svn%2Fjava%2Fgdx%2FDemoDraft%2Fsrc%2Fcom%2Fjie%2Fandroid%2Fgdx%2Fdemo
		
	}
}
