package com.rauma.lille.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rauma.lille.SpaceGame;
import com.rauma.lille.stages.BackgroundStage;
import com.rauma.lille.stages.ControllerStage;
import com.rauma.lille.stages.DefaultActorStage;
import com.rauma.lille.stages.UIStage;

public class DefaultLevelScreen extends DefaultScreen {

	protected ControllerStage controllerStage;
	protected DefaultActorStage actorStage;
	protected BackgroundStage bgStage;
	protected UIStage uiStage;

	public DefaultLevelScreen(SpaceGame game, String mapName) {
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

	@Override
	public void render(float delta) {
        Camera camera = actorStage.getCamera();
		camera.position.set(actorStage.getPlayerPosition());
        
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
	}
}