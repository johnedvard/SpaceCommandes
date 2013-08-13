package com.rauma.lille.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.network.Command;
import com.rauma.lille.network.CommandPosition;
import com.rauma.lille.stages.BackgroundStage;
import com.rauma.lille.stages.ControllerStage;
import com.rauma.lille.stages.DefaultActorStage;
import com.rauma.lille.stages.UIStage;

public class DefaultLevelScreen extends AbstractScreen {

	protected ControllerStage controllerStage;
	protected DefaultActorStage actorStage;
	protected BackgroundStage bgStage;
	protected UIStage uiStage;
	private Actor player;

	public DefaultLevelScreen(final SpaceGame game, String mapName) {
		super(game);
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getWidth();
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
		player = (Actor) actorStage.getPlayer();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(controllerStage);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		bgStage.act(delta);
		actorStage.act(delta);
		uiStage.act(delta);
		controllerStage.act(delta);
		
		bgStage.draw();
		actorStage.draw();
		uiStage.draw();
		controllerStage.draw();

		CommandPosition p = new CommandPosition("1", player.getX(), player.getY());
		game.writeToServer(new Command(p));
	}
}