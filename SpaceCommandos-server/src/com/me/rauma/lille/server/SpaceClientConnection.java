package com.me.rauma.lille.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * @author frank
 * 
 */
public class SpaceClientConnection {
	private static final Logger LOG = Logger.getLogger("SpaceClientConnection");
	
	private boolean running = true;

	private InputStreamHandler inputStreamHandler;
	private OutputStreamHandler outputStreamHandler;

	private Socket socket;

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
					while ((b = bis.read()) != -1) {
						sb.append((char)b);
						if (b == 10 || b == 13) {
							handleInput(sb.toString());
							sb.delete(0, sb.length());
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class OutputStreamHandler extends Thread {
		private BufferedOutputStream bos;
		private Queue<byte[]> queue = new LinkedList<>();
		
		public OutputStreamHandler(OutputStream outputStream) {
			this.bos = new BufferedOutputStream(outputStream);
			queue.add("hello".getBytes());
		}

		public void sendMessage(byte[] msg) {
			queue.add(msg);
		}

		@Override
		public void run() {
			try {
				while (running) {
					while (queue.size() > 0) {
						LOG.info("Sending output");
						byte[] poll = queue.poll();
						if (poll == null)
							continue;

						bos.write(poll);
					}
					bos.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleInput(String string) {
		LOG.info("Received '" + string + "'");
		outputStreamHandler.sendMessage(string.getBytes());
	}
}
