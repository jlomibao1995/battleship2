package server;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import problemdomain.Message;


public class Server{

	//private ArrayList<ClientConnection> connections;
	private ServerGUI serverGUI;
	private final int PORT = 1234;
	private ArrayList<GameConnection> currentGames;
	ArrayList<ClientConnection> connections;

	public Server(ServerGUI server) {

		//this.connections = new ArrayList<>();
		this.serverGUI = server;
		this.currentGames = new ArrayList<>();
	}

	public void connectToNetwork() throws IOException {
		
		connections = new ArrayList<>() ;

		ServerSocket listener = new ServerSocket(this.PORT);
		this.serverGUI.addMessage("Listening on port: " + PORT);

		while (listener.isBound()) {
			try {
				Socket client = listener.accept();

				InputStream inputStream = client.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

				OutputStream outputStream = client.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				
				Message username = (Message) objectInputStream.readObject();
				this.serverGUI.addMessage(username.toString());

				this.serverGUI.addMessage("Waiting for players...");

				ClientConnection connection = new ClientConnection(client, objectInputStream, objectOutputStream, username.getUsername());
				connections.add(connection);

				if (connections.size() % 2 == 0) {
					ClientConnection connection1 = connections.get(0);
					ClientConnection connection2 = connections.get(1);
					
					GameConnection game = new GameConnection(this, this.serverGUI, connection1, connection2);
					currentGames.add(game);
					Thread thread = new Thread(game);
					
					thread.start();
					
					connections.remove(connection1);
					connections.remove(connection2);
				}

			}

			catch (IOException ex) {

			} 
			catch (ClassNotFoundException e) {
			}
		}
		
		listener.close();
	}

	/**
	 * @return the connections
	 */
	public ClientConnection getConnection() {
		
		ClientConnection connection = null;
		if (connections.size() != 0) {
			connection = connections.get(0);
			connections.remove(0);
		}
		
		return connection;
	}

}
