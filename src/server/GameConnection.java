package server;

import java.io.IOException;
import java.util.ArrayList;

import problemdomain.GridButton;
import problemdomain.Message;

public class GameConnection implements Runnable {
	
	private ClientConnection connection1;
	private ClientConnection connection2;
	private ServerGUI serverGUI;
	
	public GameConnection(ServerGUI serverGUI, ClientConnection connection1, ClientConnection connection2) {

		this.connection1 = connection1;
		this.connection2 = connection2;
		this.serverGUI = serverGUI;
	}

	@Override
	public void run() {	
		
		Message message = new Message("Server", "Begin game");
		serverGUI.addMessage("Game Started!");
		
		try {
			this.connection1.getOos().writeObject(message);
			this.connection2.getOos().writeObject(message);
		} catch (IOException e1) {

		}
		
		try {
			ArrayList<GridButton> playerGrid1 = (ArrayList<GridButton>)  this.connection1.getOis().readObject();
			this.connection2.getOos().writeObject(playerGrid1);
			
			ArrayList<GridButton> playerGrid2 = (ArrayList<GridButton>)  this.connection2.getOis().readObject();
			this.connection1.getOos().writeObject(playerGrid2);
			
			serverGUI.addMessage("Sent player grids.");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		InputOutputHandler  ioHandler1 = new InputOutputHandler(this.connection2, this.connection1, this.serverGUI);
		Thread thread1 = new Thread(ioHandler1);
		thread1.start();
		
		InputOutputHandler  ioHandler2 = new InputOutputHandler(this.connection1, this.connection2, this.serverGUI);
		Thread thread2 = new Thread(ioHandler2);
		thread2.start();	
		
		try {
			thread1.join();
			thread2.join();
		} 
		catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	
	
}
