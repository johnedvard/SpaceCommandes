package com.rauma.lille.level001;

import com.badlogic.gdx.Gdx;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.screens.DefaultLevelScreen;

public class Level001Screen extends DefaultLevelScreen {

	public Level001Screen(SpaceGame game) {
		super(game);
		
		actorStage = new Level001ActorStage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), true);

		
	}
}
