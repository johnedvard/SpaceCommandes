package com.rauma.lille.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.actors.SimplePlayer;
import com.rauma.lille.network.ApplyDamageCommand;
import com.rauma.lille.network.Command;
import com.rauma.lille.network.KillCommand;
import com.rauma.lille.network.KillDeathRatioCommand;
import com.rauma.lille.network.PlayerAimedCommand;
import com.rauma.lille.network.PositionCommand;
import com.rauma.lille.network.StartGameCommand;
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
		
		float width = SpaceGame.VIRTUAL_WIDTH;
		float height = SpaceGame.VIRTUAL_HEIGHT;
		bgStage = new BackgroundStage(width, height, true);
		actorStage = new DefaultActorStage(game, width, height, true);
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
						SimplePlayer player = actorStage.getPlayer();
						actorStage.playerAimed(player.getId(),knobX, knobY, knobPercentX, knobPercentY);
						if(player != null){
							Command playerAimedCommand = new PlayerAimedCommand(player.getId(),knobX, knobY, knobPercentX, knobPercentY);
							game.writeToServer(playerAimedCommand);
						}
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
        Gdx.graphics.setVSync(false);
		
		bgStage.act(delta);
		actorStage.act(delta);
		uiStage.act(delta);
		controllerStage.act(delta);
		
		bgStage.draw();
		actorStage.draw();
		uiStage.draw();
		controllerStage.draw();

		SimplePlayer player = actorStage.getPlayer();
		if(player != null && (Math.abs(player.getX() - prevX) > 1 || (Math.abs(player.getY() - prevY) > 1))){
			Body body = player.getBody();
			if(body != null){
				PositionCommand p = new PositionCommand(player.getId(),player.getX(), player.getY(),body.getAngle());
				game.writeToServer(p);
				prevX = player.getX();
				prevY = player.getY();
			}
		}
	}

	public void createNewGame(StartGameCommand startGameCommand) {
		uiStage.createNewGame(startGameCommand);
		actorStage.createNewGame(startGameCommand);
	}

	public void updatePlayerPos(PositionCommand commandPos) {
		actorStage.updatePlayerPos(commandPos);
	}

	public void playerAimedCommand(PlayerAimedCommand playerAimedCommand) {
		float knobX = playerAimedCommand.getKnobX();
		float knobY = playerAimedCommand.getKnobY();
		float knobPercentX = playerAimedCommand.getKnobPercentX();
		float knobPercentY = playerAimedCommand.getKnobPercentY();
		int playerId = playerAimedCommand.getPlayerId();
		actorStage.playerAimed(playerId, knobX, knobY, knobPercentX, knobPercentY);
		
	}

	public SpaceGame getGame() {
		return game;
	}

	public void applyDamageCommand(ApplyDamageCommand applyDmgCommand) {
		actorStage.applyDamageCommand(applyDmgCommand);
	}

	public void killCommand(KillCommand killCommand) {
		actorStage.killCommand(killCommand);
		
	}

	public void killDeathRationCommand(KillDeathRatioCommand kdratioCommmad) {
		uiStage.killDeathRationCommand(kdratioCommmad);
		
	}
	
}