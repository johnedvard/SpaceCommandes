package com.rauma.lille.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.actors.SimplePlayer;
import com.rauma.lille.network.Command;
import com.rauma.lille.network.CommandPosition;
import com.rauma.lille.network.CommandStartGame;
import com.rauma.lille.stages.BackgroundStage;
import com.rauma.lille.stages.ControllerStage;
import com.rauma.lille.stages.DefaultActorStage;
import com.rauma.lille.stages.UIStage;

public class DefaultLevelScreen extends DefaultScreen {

	protected ControllerStage controllerStage;
	protected DefaultActorStage actorStage;
	protected BackgroundStage bgStage;
	protected UIStage uiStage;

	public DefaultLevelScreen(final SpaceGame game, String mapName) {
		super(game);
		
		float width = SpaceGame.SCREEN_WIDTH;
		float height = SpaceGame.SCREEN_HEIGHT;
		bgStage = new BackgroundStage(width, height, true);
		actorStage = new DefaultActorStage(width, height, true);
		actorStage.initMap(mapName);
		
		uiStage = new UIStage(width, height, true);
		controllerStage = new ControllerStage(width, height, true);

		controllerStage.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actor instanceof Touchpad) {
					Touchpad touchpad = (Touchpad) actor;
					float knobPercentX = touchpad.getKnobPercentX();
					float knobPercentY = touchpad.getKnobPercentY();
					float knobX = touchpad.getKnobX();
					float knobY = touchpad.getKnobY();
					
					if (actor.getName().equals("touchpadLeft")) {
						actorStage.playerMoved(knobX, knobY, knobPercentX, knobPercentY);
					} else if (actor.getName().equals("touchpadRight")) {
						actorStage.playerAimed(knobX, knobY, knobPercentX, knobPercentY);
					}
				}
			}
		});
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(controllerStage);
	}

	private float prevX, prevY = 0;
	
	@Override
	public void render(float delta) {
		 // the following code clears the screen with the given RGB color (green)
        Gdx.gl.glClearColor( 0.0f, 0.5f, 0.0f, 1f );
        Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
        
		
		bgStage.act(delta);
		actorStage.act(delta);
		uiStage.act(delta);
		controllerStage.act(delta);
		
		bgStage.draw();
		actorStage.draw();
		uiStage.draw();
		controllerStage.draw();

		SimplePlayer player = actorStage.getPlayer();
		if(player != null && (player.getX() != prevX || player.getY() != prevX)){
			Command p = new CommandPosition(player.getId(),player.getX(), player.getY());
			game.writeToServer(p);
		}
	}

	public void createNewGame(CommandStartGame startGameCommand) {
		actorStage.createNewGame(startGameCommand);
	}

	public void updatePlayerPos(CommandPosition commandPos) {
		actorStage.updatePlayerPos(commandPos);
	}
	
}