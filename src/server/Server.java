package server;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import problemdomain.Message;
/**
 * Creates a server which listens to the specified port.
 * @author Jean
 * @version October 6, 2020
 *
 */
public class Server{

	private ServerGUI serverGUI; //the server GUI
	ArrayList<ClientConnection> connections; //contains list of client sockets 

	/**
	 * User-defined constructor for the class.
	 * @param server server GUI
	 */
	public Server(ServerGUI server) {
		this.serverGUI = server;
	}

	/**
	 * Will create a ServerSocket which listens to the specified port and accepts client
	 * @throws IOException when there is an error with the connection 
	 */
	public void connectToNetwork() throws IOException {
		
		connections = new ArrayList<>() ;
		
		//prompt user for port number
		//int port = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter port number: "));
		int port = 1234;

		ServerSocket listener = new ServerSocket(port);
		this.serverGUI.addMessage("Listening on port: " + port);

		while (listener.isBound()) {
			try {
				//when a client connects obtain the socket and streams
				Socket client = listener.accept();

				InputStream inputStream = client.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

				OutputStream outputStream = client.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				
				//to obtain the client usernames
				Message username = (Message) objectInputStream.readObject();
				this.serverGUI.addMessage(username.toString());

				this.serverGUI.addMessage("Waiting for players...");

				//create the client connection
				//if there are two clients connected, connect the streams, then start a thread
				ClientConnection connection = new ClientConnection(client, objectInputStream, objectOutputStream, username.getUsername());
				connections.add(connection);

				if (connections.size() % 2 == 0) {
					ClientConnection connection1 = connections.get(0);
					ClientConnection connection2 = connections.get(1);
					
					GameConnection game = new GameConnection(this, this.serverGUI, connection1, connection2);
					Thread thread = new Thread(game);
					
					thread.start();
					
					//once the clients have been connected remove them from the list
					connections.remove(connection1);
					connections.remove(connection2);
				}

			}

			catch (IOException e) {
				e.printStackTrace();
				this.serverGUI.addMessage("Unable to connect to client.");

			} 
			catch (ClassNotFoundException e) {
				this.serverGUI.addMessage("Unable to connect to client.");
				e.printStackTrace();
			}
		}
		
		listener.close();
	}

	/**
	 * @return connection client connection 
	 */
	public ClientConnection getConnection() {
		
		//if the list contains a client connection return that connection
		ClientConnection connection = null;
		if (connections.size() != 0) {
			connection = connections.get(0);
			connections.remove(0);
		}
		
		return connection;
	}

}
