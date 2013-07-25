package com.rauma.lille.level002;

import com.badlogic.gdx.Gdx;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.screens.DefaultLevelScreen;

public class Level002Screen extends DefaultLevelScreen {
	
	public Level002Screen(SpaceGame game) {
		super(game);
		actorStage = new Level002ActorStage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
	}
}
