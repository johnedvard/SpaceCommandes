package com.me.rauma.lille.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Random;

import javax.print.DocFlavor.INPUT_STREAM;

public class TestClient {

	public TestClient() throws InterruptedException {

		int port = new Random().nextInt(1024) + 1024;

		SpaceServer spaceServer = new SpaceServer(port);

		while (!spaceServer.isRunning()) {
			Thread.sleep(1000);
		}

		Thread.sleep(2000);

		Socket socket = null;
		try {
			try {
				String server = null;
				socket = new Socket(server, port);
				final InputStream inputStream = socket.getInputStream();
				Thread input = new Thread() {
					public void run() {
						int b = -1;
						try {
							while ((b = inputStream.read()) != -1) {
								if(b == 10 || b == 13) {
									System.out.println();
								} else {
									System.out.print((char) b);
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					};
				};
				input.start();

				final OutputStream outputStream = socket.getOutputStream();
				Thread output = new Thread() {
					public void run() {
						try {
							while (true) {
								outputStream
										.write((new Date().toString()+"\n").getBytes());
								Thread.sleep(1000);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				};
				output.start();

				input.join();
				output.join();
			} finally {
				if (socket != null) {
					socket.close();
				}
				
				spaceServer.stop();
			}
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.err.println("Exception in thread " + t + " -> " + e.getMessage());
				e.printStackTrace();
			}
		});
		new TestClient();
	}
}
