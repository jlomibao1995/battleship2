package server;

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
		serverGUI.addMessage("Game Started!");
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
