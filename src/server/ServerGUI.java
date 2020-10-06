package server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.*;

/**
 * GUI for the server which display a window that shows game traffic.
 * @author Jean
 *@version October 6, 2020
 */
public class ServerGUI {

	private JFrame frame; //window for the server display

	//list of messages and traffic from clients
	private JList trafficList;
	private DefaultListModel trafficListModel;

	/**
	 * User-defined constructor for the class.
	 */
	public ServerGUI() {

		//add panels to the window then display it
		frame = new JFrame("BattleShip Server");

		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		this.frame.setSize(600, 600);

		JPanel trafficPanel = createTrafficPanel();
		JPanel buttonPanel = createButtonPanel();

		trafficPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10 ,30));
		
		this.frame.add(trafficPanel, BorderLayout.CENTER);
		this.frame.add(buttonPanel, BorderLayout.SOUTH);
		
		display();
		
		//obtain a server which will listen for clients
		Server server = new Server(this);
		try {
			
			server.connectToNetwork();
			
		} catch (IOException e) {
			this.addMessage("Unable to connect to port.");
			e.printStackTrace();
		}
	}

	/**
	 * Creates window panel which will display traffic and messages from clients
	 * @return panel traffic panel
	 */
	public JPanel createTrafficPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel title = new JLabel("Traffic", SwingConstants.CENTER);
		
		trafficListModel = new DefaultListModel();
		trafficList = new JList(trafficListModel);
		
		JScrollPane scrollPane = new JScrollPane(trafficList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel.add(scrollPane);
		panel.add(title, BorderLayout.NORTH);
		return panel;
	}
	
	/**
	 * Creates buttons for connecting and disconnecting to port.
	 * @return panel buttons panel
	 */
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JButton exit = new JButton("Exit");
		
		exit.addActionListener((ActionEvent a) -> {
			System.exit(0);
		});
		
		panel.add(exit);
		
		return panel;
	}
	
	/**
	 * Adds and displays messages to the traffic panel.
	 * @param text
	 */
	public void addMessage(String text) {
		
		this.trafficListModel.addElement(text);
	}

	/**
	 * Displays the server GUI window.
	 */
	public void display() {
		//this.frame.pack();
		this.frame.setVisible(true);
	}
}
