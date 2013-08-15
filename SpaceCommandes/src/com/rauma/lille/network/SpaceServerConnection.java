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


public class SpaceServerConnection{
	private static final Logger LOG = Logger.getLogger("SpaceServerConnection");
	
	private boolean running = true;

	private InputStreamHandler inputStreamHandler;
	private OutputStreamHandler outputStreamHandler;

	private Socket socket;
	private Queue<byte[]> outputQueue = new LinkedList<byte[]>();
	private Json json;
	
	public SpaceServerConnection(Socket s) {
		LOG.info("Client connection created: " + s);
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
					while (outputQueue.size() > 0) {
//						LOG.info("Sending output");
						byte[] poll = outputQueue.poll();
						if (poll == null)
							continue;

						bos.write(poll);
					}
					bos.flush();
				}
			} catch (IOException e) {
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
			Command c = json.fromJson(Command.class, string);
			switch (c.type) {
			case Command.POSITION:
				System.out.println("got a position");
				break;
			case Command.START_GAME:
				System.out.println("switch to game-screen and play the game. set up the game with the state from the server");
				break;
			default:
				System.out.println("got something else: " + string);
				break;
			}
		}
	}
	
}
