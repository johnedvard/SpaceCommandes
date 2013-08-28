package com.rauma.lille.stages;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.rauma.lille.network.Command;
import com.rauma.lille.network.ScoreCommand;
import com.rauma.lille.network.StartGameCommand;


/**
 * This class will be responsible for showing real time information during the
 * events of the game. Thinks like health, power, money, time and so on. There should be no physics here. 
 * 
 * @author frank
 * 
 */
public class UIStage extends AbstractStage {

	private int playerId = -1;
	private Vector2 killDeathRatio = new Vector2(0,0);
	private Label lblKillDeathRatio;
	private List<Command> commandsToExecute = new ArrayList<Command>();
	
	public UIStage(float width, float height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		
        BitmapFont defaultFont = new BitmapFont();
        LabelStyle style = new LabelStyle(defaultFont,Color.WHITE); 
		lblKillDeathRatio = new Label( "0,0",style);
		lblKillDeathRatio.setX(50);
		lblKillDeathRatio.setY((getHeight())-50);
		addActor(lblKillDeathRatio);
		
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		executeCommandQueue();
	}
	
	@Override
	public void draw() {
		super.draw();
	}
	private void executeCommandQueue() {
		while(commandsToExecute.size()>0){
			Command c = commandsToExecute.remove(0);
			if (c instanceof ScoreCommand) {
				ScoreCommand scoreCommand = (ScoreCommand) c;
				killDeathRatio = scoreCommand.getKillDeathRatio(playerId);
				System.out.println("score is: "+ killDeathRatio);
				lblKillDeathRatio.setText((int)killDeathRatio.x+","+(int)killDeathRatio.y);
			}
		}
		
	}
	
	public void scoreCommand(ScoreCommand scoreCommand) {
		commandsToExecute.add(scoreCommand);
		System.out.println("adding score command to ui stage");
	}

	public void createNewGame(StartGameCommand startGameCommand) {
		playerId = startGameCommand.getPlayerId();
	}

}
