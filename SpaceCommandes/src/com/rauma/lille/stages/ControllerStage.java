package com.rauma.lille.stages;

import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * This stage should handle controllers, such as Touchpad and keyboard input.
 * @author john
 *
 */
public class ControllerStage extends AbstractStage{
	private Touchpad touchpadLeft; 
	private Touchpad touchpadRight;
	
	private HashSet<ChangeListener> changeListeners	= new HashSet<ChangeListener>();
	
	public ControllerStage(int width, int height, boolean keepAspectRatio) {
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
	
	private Touchpad createNewTouchpad(){
		Texture touchpadTexture = new Texture(Gdx.files.internal("data/libgdx.png"));
		touchpadTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);     
		TextureRegion background = new TextureRegion(touchpadTexture, 80, 80, 17, 17);
		TextureRegion knob = new TextureRegion(touchpadTexture, 50, 191, 20, 20);
		NinePatch ninePatchTouchpadBg = new NinePatch(background);
		NinePatch ninePatchTouchpadKnob = new NinePatch(knob);
		NinePatchDrawable backgroundDrawable = new NinePatchDrawable(ninePatchTouchpadBg);
		NinePatchDrawable knobDrawable = new NinePatchDrawable(ninePatchTouchpadKnob);
		Touchpad touchpad = new Touchpad(5, new Touchpad.TouchpadStyle(backgroundDrawable, knobDrawable));
		touchpad.setSize(50, 50);
		return touchpad;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 stageCoords = this.screenToStageCoordinates(new Vector2(screenX,screenY));
		if(screenX< getWidth()/2){
			touchpadLeft.setX(stageCoords.x-touchpadLeft.getWidth()/2);
			touchpadLeft.setY(stageCoords.y-touchpadLeft.getHeight()/2);
			touchpadLeft.setVisible(true);
			touchpadLeft.validate();
		}else{
			touchpadRight.setX(stageCoords.x-touchpadLeft.getWidth()/2);
			touchpadRight.setY(stageCoords.y-touchpadLeft.getHeight()/2);
			touchpadRight.setVisible(true);
			touchpadRight.validate();
		}
		return super.touchDown(screenX, screenY, pointer, button) && true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(screenX< getWidth()/2){
			touchpadLeft.setVisible(false);
		}else{
			touchpadRight.setVisible(false);
		}
		return super.touchUp(screenX, screenY, pointer, button);
	}
	
}
