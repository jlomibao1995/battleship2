package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String username;
	
	
	public ClientConnection(Socket socket, ObjectInputStream ois, ObjectOutputStream oos, String username) {
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
		this.username = username;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @return the ois
	 */
	public ObjectInputStream getOis() {
		return ois;
	}

	/**
	 * @return the oos
	 */
	public ObjectOutputStream getOos() {
		return oos;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	
	

}
