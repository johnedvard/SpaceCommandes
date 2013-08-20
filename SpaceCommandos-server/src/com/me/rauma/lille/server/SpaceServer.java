package com.me.rauma.lille.server;

import java.io.IOException;
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
	private List<Game> games = new ArrayList<Game>();
	
	private boolean running = false;

	private int numPlayersPrGame = 2;

	public SpaceServer(final int port) {
		new Thread() {
			public void run() {
				ServerSocket ss = null;
				try {
					ss = new ServerSocket(port);
					LOG.log(Level.INFO, "Started server");
					running = true;
					while (running) {
						LOG.log(Level.INFO, "Waiting for client connection");
						try {
							Socket socket = ss.accept();
							LOG.log(Level.INFO, "Client accepted");
							init(socket);
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

				try {
					if(ss != null) ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				LOG.log(Level.INFO, "Server stopped");
			}
		}.start();
	}


	private synchronized void init(Socket socket) throws IOException {
		createSpaceClient(socket);
		checkForGames();
	}


	private void checkForGames() {
		while(clients.size() >= numPlayersPrGame && clients.size() % numPlayersPrGame == 0){
			createGame();
		}
	}

	private synchronized void createGame() {
		LOG.log(Level.INFO, "Creating GAME!");
		// get clients for the current game.
		List<SpaceClientConnection> clientsToPlayTogether = new ArrayList<SpaceClientConnection>();
		for(int i = 0; i<numPlayersPrGame; i++){
			SpaceClientConnection spaceClientConnection = clients.remove(clients.size()-1);
			clientsToPlayTogether.add(spaceClientConnection);
		}
		Game game = new Game(clientsToPlayTogether, this);
		games.add(game);
	}
	
	public void gameEnded(Game game) {
		System.out.println("game ended");
		games.remove(game);
		List<SpaceClientConnection> clientsInGame = game.getClientsInGame();
		for (SpaceClientConnection spaceClientConnection : clientsInGame) {
			if(spaceClientConnection.isRunning())
				clients.add(spaceClientConnection);
		}
		checkForGames();
	}

	private synchronized void createSpaceClient(Socket socket) throws IOException {
		LOG.log(Level.INFO, "Creating client");
		SpaceClientConnection client = new SpaceClientConnection(socket, this);
		clients.add(client);
	}
	
	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
	}

	public static void main(String[] args) {
		int port = 1337;
		if(args.length>0){
			port = Integer.valueOf(args[0]);
		}
		new SpaceServer(port);
	}


	public void clientDisconnected(SpaceClientConnection spaceClientConnection) {
		clients.remove(spaceClientConnection);
	}
}
