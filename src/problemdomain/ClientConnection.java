package problemdomain;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ClientConnection(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {

		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
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
	

}
