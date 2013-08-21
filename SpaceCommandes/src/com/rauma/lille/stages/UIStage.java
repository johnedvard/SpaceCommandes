package com.rauma.lille.stages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.rauma.lille.network.Command;
import com.rauma.lille.network.KillDeathRatioCommand;
import com.rauma.lille.network.PositionCommand;
import com.rauma.lille.network.StartGameCommand;


/**
 * This class will be responsible for showing real time information during the
 * events of the game. Thinks like health, power, money, time and so on. There should be no physics here. 
 * 
 * @author frank
 * 
 */
public class UIStage extends AbstractStage {

	private BitmapFont defaultFont = new BitmapFont();
	private LabelStyle lblStyle = new LabelStyle(defaultFont,Color.WHITE);
	private Label lblKillDeathRatio = new Label("kills: 0 , deaths: 0", lblStyle);
	private List<Command> commands = new ArrayList<Command>();
	// the id of the player we are playing.
	private int id;
	
	
	public UIStage(float width, float height, boolean keepAspectRatio) {
		super(width, height, keepAspectRatio);
		lblKillDeathRatio.setX(50);
		lblKillDeathRatio.setY(getHeight()-50);
		addActor(lblKillDeathRatio);
	}
	
	private void executeCommandQueue(){
		while(commands.size()>0){
			Command command = commands.remove(0);
			if (command instanceof KillDeathRatioCommand) {
				KillDeathRatioCommand kdratioCommand = (KillDeathRatioCommand) command;
				HashMap<String,Vector2> stats = kdratioCommand.getKdratios();
				System.out.println("stats: " + stats);
				System.out.println("id: " + id);
				Vector2 myStats = stats.get(""+id);
				System.out.println("myStats: " + myStats);
				lblKillDeathRatio.setText("kills: "+(int)myStats.x +" , deaths: "+(int)myStats.y);
			
			}
		}
	}
	
	@Override
	public void act(float delta) {
		executeCommandQueue();
	}
	
	@Override
	public void draw() {
		super.draw();
	}

	public void killDeathRationCommand(KillDeathRatioCommand kdratioCommmad) {
		System.out.println("got kd ratio command");
		commands.add(kdratioCommmad);
	}

	public void createNewGame(StartGameCommand startGameCommand) {
		System.out.println("got start new game command");
		this.id = startGameCommand.getPlayerId();
	}
}
