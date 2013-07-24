package com.rauma.lille.level001;

import com.badlogic.gdx.Gdx;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.screens.DefaultLevelScreen;
import com.rauma.lille.stages.ActorStage;

public class Level001Screen extends DefaultLevelScreen {

	public Level001Screen(SpaceGame game) {
		super(game);
		
		actorStage = new ActorStage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), true);

	}
}
