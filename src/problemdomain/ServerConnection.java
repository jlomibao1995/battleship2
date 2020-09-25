package problemdomain;

import java.io.*;
import java.net.Socket;

public class ServerConnection implements Runnable {

	private Socket socket;
	private OutputStream os;
	private ObjectOutputStream oos;
	private InputStream is;
	private ObjectInputStream ois;
	Server server;

	boolean shouldRun = true;

	public ServerConnection(Socket socket, Server server) {

		this.socket = socket;
		this.server = server;
	}
	
	private void sendMessageToClient(Message message) {
		
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void receiveMessageFromClient() {

		try {
			Message input = (Message) ois.readObject();
			sendMessageToClient(input);
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {

		try {

			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);

			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			
			while(ois.available() == 0) {
				
				try {
					Thread.sleep(1);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
					close();
				}
			}
			
			while (socket.isConnected()) {
				
				receiveMessageFromClient();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {

		try {

			oos.close();
			ois.close();
			socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
