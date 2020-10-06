package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class containing the socket and streams for each client.
 * @author Jean
 *@version October 6, 2020
 */
public class ClientConnection {
	
	private Socket socket; 
	private ObjectInputStream ois; 
	private ObjectOutputStream oos; 
	private String username; 
	
	/**
	 * User defined constructor for the class.
	 * @param socket client socket
	 * @param ois ObjectInputStream 
	 * @param oos ObjectOutputStream
	 * @param username client username
	 */
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
	 * @return the ObjectInputStream 
	 */
	public ObjectInputStream getOis() {
		return ois;
	}

	/**
	 * @return the ObjectOutputStream
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
