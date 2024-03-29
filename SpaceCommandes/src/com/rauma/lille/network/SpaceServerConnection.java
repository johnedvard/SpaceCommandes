package com.rauma.lille.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Json;
import com.rauma.lille.SpaceGame;


public class SpaceServerConnection{
	private static final Logger LOG = Logger.getLogger("SpaceServerConnection");
	
	private boolean running = true;

	private InputStreamHandler inputStreamHandler;
	private OutputStreamHandler outputStreamHandler;

	private Socket socket;
	private Queue<byte[]> outputQueue = new LinkedList<byte[]>();
	private Json json;
	private SpaceGame game;
	public SpaceServerConnection(Socket s, final SpaceGame spaceGame) {
		LOG.info("Client connection created: " + s);
		game = spaceGame;
		this.socket = s;
		json = new Json();
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		
		inputStreamHandler = new InputStreamHandler(inputStream);
		inputStreamHandler.start();
		outputStreamHandler = new OutputStreamHandler(outputStream);
		outputStreamHandler.start();
		
	}
	
	class OutputStreamHandler extends Thread {
		private BufferedOutputStream bos;
		public OutputStreamHandler(OutputStream outputStream) {
			bos = new BufferedOutputStream(outputStream);
		}
		@Override
		public void run() {
			try {
				while (running) {
					while (outputQueue.peek() != null) {
//						LOG.info("Sending output");
						byte[] poll = outputQueue.poll();
						if (poll == null)
							continue;

						bos.write(poll);
					}
					outputQueue.clear();
					bos.flush();
					Thread.sleep(10);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			running = false; 
		}
	}
	public boolean stop() {
		running = false;
		try {
			inputStreamHandler.join(1000);
			outputStreamHandler.join(1000);
			
			socket.dispose();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	class InputStreamHandler extends Thread {
		private BufferedInputStream bis;

		public InputStreamHandler(InputStream inputStream) {
			this.bis = new BufferedInputStream(inputStream);
		}

		@Override
		public void run() {
			System.out.println("ready to receive input");
			int b = -1;
			StringBuilder sb = new StringBuilder();
			try {
				while (running) {
					LOG.info("Reading input");
					while (running && (b = bis.read()) != -1) {
						sb.append((char)b);
						if (b == 10 || b == 13) {
							handleInput(sb.toString());
							sb.delete(0, sb.length());
						}
					}
					running = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// add all bytes to a queue, which should be written to the server
	// rapidly
	public void writeToServer(byte[] b) {
		outputQueue.add(b);
	}
	
	private void handleInput(String string) {
//		LOG.info("\ngot message from server: " + string);
		if(string.startsWith("{")){ //FIXME check if the message is a json message
			Object c = json.fromJson(null, string);
			if (c instanceof PositionCommand) {
				PositionCommand commandPos = (PositionCommand) c;
				game.updatePlayerPos(commandPos);
			}else if (c instanceof StartGameCommand) {
				StartGameCommand cmdStarGame = (StartGameCommand) c;
				game.createNewGame(cmdStarGame);
			}else if (c instanceof PlayerAimedCommand) {
				PlayerAimedCommand playerAimedCommand = (PlayerAimedCommand) c;
				game.playerAimedCommand(playerAimedCommand);
			}else if (c instanceof ApplyDamageCommand) {
				ApplyDamageCommand applyDmgCommand = (ApplyDamageCommand) c;
				game.applyDamageCommand(applyDmgCommand);
			} else if (c instanceof KillCommand) {
				KillCommand killCommand = (KillCommand) c;
				game.killCommand(killCommand);
				System.out.println("killl");
			} else if (c instanceof EndGameCommand) {
				EndGameCommand endCommand = (EndGameCommand) c;
				game.end(endCommand);
			}else if (c instanceof ScoreCommand) {
				ScoreCommand scoreCommand = (ScoreCommand) c;
				game.scoreCommand(scoreCommand);
				System.out.println("score: ");
				System.out.println(scoreCommand.getKillDeathRatios());
			}
			else {
				System.out.println("got something else: " + string);
			}
		}
	}
	
}
