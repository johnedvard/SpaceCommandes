package com.me.rauma.lille.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.me.rauma.lille.server.Game.CommandMessageListener;
import com.rauma.lille.network.Command;

/**
 * @author frank
 * 
 */
public class SpaceClientConnection {
	private static final Logger LOG = Logger.getLogger("SpaceClientConnection");
	
	private boolean running = true;

	private InputStreamHandler inputStreamHandler;
	private OutputStreamHandler outputStreamHandler;

	private List<CommandMessageListener> commandListeners = new ArrayList<CommandMessageListener>();
	private Socket socket;
	private Json json = new Json();

	private int playerId = -1;
	public SpaceClientConnection(Socket s) throws IOException {
		LOG.info("Client connection created: " + s);
		this.socket = s;
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();

		inputStreamHandler = new InputStreamHandler(inputStream);
		inputStreamHandler.start();
		outputStreamHandler = new OutputStreamHandler(outputStream);
		outputStreamHandler.start();
	}

	public boolean stop() {
		running = false;
		try {
			inputStreamHandler.join(1000);
			outputStreamHandler.join(1000);
			
			socket.close();
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

	class OutputStreamHandler extends Thread {
		private BufferedOutputStream bos;
		private Queue<byte[]> queue = new LinkedList<byte[]>();
		
		public OutputStreamHandler(OutputStream outputStream) {
			this.bos = new BufferedOutputStream(outputStream);
		}

		public void sendMessage(byte[] msg) {
			queue.add(msg);
		}

		@Override
		public void run() {
			try {
				while (running) {
					while (queue.peek() != null) {
//						LOG.info("Sending output");
						byte[] poll = queue.poll();
						if (poll == null)
							continue;

						bos.write(poll);
					}
					queue.clear();
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

	private void handleInput(String string) {
//		LOG.info("Received '" + string + "'");
		//reply the client with the same message it sent
//		outputStreamHandler.sendMessage(string.getBytes());
		// send the input(command as a json) we get to all the listeners registered on this connection
		for(CommandMessageListener cml : commandListeners){
			cml.sendMessage(string);
		}
	}

	public synchronized void sendMessage(Command c) {
		byte[] b;
		String string = (json.toJson(c,Command.class)+ "\n");
		b = string.getBytes();
		outputStreamHandler.sendMessage(b);
	}

	public void addCommandMessageListener(CommandMessageListener listener) {
		commandListeners.add(listener);
	}

	public void setId(int yourId) {
		this.playerId  = yourId;
	}
	
}
