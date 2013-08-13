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


public class SpaceServerConnection{
	private static final Logger LOG = Logger.getLogger("SpaceServerConnection");
	
	private boolean running = true;

	private InputStreamHandler inputStreamHandler;
	private OutputStreamHandler outputStreamHandle;

	private Socket socket;
	private Queue<byte[]> outputQueue = new LinkedList<byte[]>();
	
	public SpaceServerConnection(Socket s) {
		LOG.info("Client connection created: " + s);
		this.socket = s;
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		
		inputStreamHandler = new InputStreamHandler(inputStream);
		inputStreamHandler.start();
		outputStreamHandle = new OutputStreamHandler(outputStream);
		outputStreamHandle.start();
		
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
						LOG.info("Sending output");
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
	
	class InputStreamHandler extends Thread {
		private BufferedInputStream bis;

		public InputStreamHandler(InputStream inputStream) {
			this.bis = new BufferedInputStream(inputStream);
		}
		
	}

	// add all bytes to a queue, which should be written to the server
	// rapidly
	public void writeToServer(byte[] b) {
		outputQueue.add(b);
	}
}
