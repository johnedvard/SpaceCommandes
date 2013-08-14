package com.rauma.lille.stages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * This stage should handle controllers, such as Touchpad and keyboard input.
 * 
 * @author john
 * 
 */
public class ControllerStage extends AbstractStage {
	private Touchpad touchpadLeft;
	private Touchpad touchpadRight;

	public ControllerStage(float width, float height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		init();
	}

	private void init() {
		touchpadLeft = createNewTouchpad();
		touchpadLeft.setName("touchpadLeft");
		touchpadLeft.setVisible(false);

		touchpadRight = createNewTouchpad();
		touchpadRight.setName("touchpadRight");
		touchpadRight.setVisible(false);

		addActor(touchpadLeft);
		addActor(touchpadRight);
	}

	private Touchpad createNewTouchpad() {
		// Create a touchpad skin
		Skin touchpadSkin = new Skin();
		Texture background = new Texture("data/touchBackground.png");
		Texture knob = new Texture("data/touchKnob.png");

		// Set background and knob image
		touchpadSkin.add("touchBackground", background);
		touchpadSkin.add("touchKnob", knob);

		// Create TouchPad Style
		TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
		// Create Drawable's from TouchPad skin
		Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
		Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");

		// Apply the drawables to the TouchPad Style
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;

		Touchpad touchpad = new Touchpad(5, touchpadStyle);
		return touchpad;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 stageCoords = screenToStageCoordinates(new Vector2(screenX,
				screenY));

		if (stageCoords.x < getWidth() / 2) {
			touchpadLeft.setX(stageCoords.x - touchpadLeft.getWidth() / 2);
			touchpadLeft.setY(stageCoords.y - touchpadLeft.getHeight() / 2);
			touchpadLeft.setVisible(true);
			touchpadLeft.validate();
		} else {
			touchpadRight.setX(stageCoords.x - touchpadRight.getWidth() / 2);
			touchpadRight.setY(stageCoords.y - touchpadRight.getHeight() / 2);
			touchpadRight.setVisible(true);
			touchpadRight.validate();
		}
		return super.touchDown(screenX, screenY, pointer, button) && true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		boolean b = super.touchUp(screenX, screenY, pointer, button);
		touchpadLeft.setVisible(touchpadLeft.isTouched());
		touchpadRight.setVisible(touchpadRight.isTouched());

		return b;
	}

}
