package problemdomain;

public class GameConnection implements Runnable {
	
	private ClientConnection connection1;
	private ClientConnection connection2;
	
	

	public GameConnection(ClientConnection connection1, ClientConnection connection2) {

		this.connection1 = connection1;
		this.connection2 = connection2;
	}

	@Override
	public void run() {
		
		InputOutputHandler  ioHandler1 = new InputOutputHandler(this.connection1, this.connection2);
		Thread thread1 = new Thread(ioHandler1);
		thread1.start();
		
		InputOutputHandler  ioHandler2 = new InputOutputHandler(this.connection2, this.connection1);
		Thread thread2 = new Thread(ioHandler2);
		thread2.start();	
	}
	
	
}
