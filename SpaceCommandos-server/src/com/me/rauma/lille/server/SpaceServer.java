package com.me.rauma.lille.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author frank
 * 
 */
public class SpaceServer {
	private static final Logger LOG = Logger.getLogger("SpaceServer");

	private List<SpaceClientConnection> clients = new ArrayList<SpaceClientConnection>();

	private boolean running = false;

	public SpaceServer(final int port) {
		new Thread() {
			public void run() {
				try {
					ServerSocket ss = new ServerSocket(port);
					LOG.log(Level.INFO, "Started server");
					running = true;
					while (running) {
						LOG.log(Level.INFO, "Waiting for client connection");
						try {
							Socket socket = ss.accept();
							LOG.log(Level.INFO, "Client accepted");
							SpaceClientConnection client = new SpaceClientConnection(
									socket);
							clients.add(client);
						} catch (Exception e) {
							e.printStackTrace();
							LOG.log(Level.WARNING, "Failed to accept client", e);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					LOG.log(Level.SEVERE, "Failed to start server", e);
				}

				for (SpaceClientConnection client : clients) {
					client.stop();
				}

				LOG.log(Level.INFO, "Server stopped");
			};
		}.start();
	}

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
	}

	public static void main(String[] args) {
		new SpaceServer(Integer.valueOf(args[0]));
	}
}
