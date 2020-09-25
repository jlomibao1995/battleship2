package problemdomain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	private ServerSocket serverSocket;
	private ArrayList<ServerConnection> connections;
	
	public Server() {
		try {
			serverSocket = new ServerSocket(1234);
			while (true) {
				Socket socket = serverSocket.accept();
				
				ServerConnection sc = new ServerConnection(socket, this);
				Thread thread = new Thread(sc);
				thread.start();
				connections.add(sc);
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
