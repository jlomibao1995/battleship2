package problemdomain;

import java.io.*;
import java.net.Socket;

import gui.ClientGUI;

public class ServerConnection implements Runnable {

	private Socket server;
	private ClientGUI client;
	private ObjectInputStream ois;

	
	public ServerConnection(ClientGUI client, ObjectInputStream ois, Socket server) {
		this.server = server;
		this.client = client;
		this.ois = ois;
	}

	@Override
	public void run() {

		while (!server.isClosed())
		{
			Message receive;
			try {
				receive = (Message) ois.readObject();
				client.addMessage(receive.toString());
				
			} 
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

}
