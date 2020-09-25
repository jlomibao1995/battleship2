package problemdomain;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

	private ArrayList<ClientConnection> connections;

	public Server() {

		this.connections = new ArrayList<>();

	}

	public void connectToNetwork() throws IOException {

		ServerSocket listener = new ServerSocket(1234);

		while (listener.isBound()) {
			try {
				Socket client = listener.accept();

				System.out.println("Client connected.");

				InputStream inputStream = client.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

				OutputStream outputStream = client.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

				System.out.println("Waiting for messages...");

				ClientConnection connection = new ClientConnection(client, objectInputStream, objectOutputStream);
				connections.add(connection);

				if (connections.size()%2 == 0) {

					ClientConnection connection1 = connections.get(0);
					ClientConnection connection2 = connections.get(1);

					GameConnection gameConnection = new GameConnection(connection1, connection2);
					Thread thread = new Thread(gameConnection);
					thread.start();

					connections.remove(0);
					connections.remove(1);

				}

			}

			catch (IOException ex) {

				listener.close();
			}

		}
	}

}
