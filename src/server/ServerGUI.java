package server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.*;

public class ServerGUI {

	private JFrame frame;

	private JList trafficList;
	private DefaultListModel trafficListModel;

	public ServerGUI() {

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
		
		Server server = new Server(this);
		try {
			
			server.connectToNetwork();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JPanel createTrafficPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		//JButton connectButton = new JButton("Connect");
		
		JLabel title = new JLabel("Traffic", SwingConstants.CENTER);
		
		trafficListModel = new DefaultListModel();
		trafficList = new JList(trafficListModel);
		
		JScrollPane scrollPane = new JScrollPane(trafficList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel.add(scrollPane);
		panel.add(title, BorderLayout.NORTH);
		//panel.add(connectButton, BorderLayout.SOUTH);
		return panel;
	}
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JButton exit = new JButton("Exit");
		
		exit.addActionListener((ActionEvent a) -> {
			System.exit(0);
		});
		
		panel.add(exit);
		
		return panel;
	}
	
	public void addMessage(String text) {
		
		this.trafficListModel.addElement(text);
	}

	public void display() {
		//this.frame.pack();
		this.frame.setVisible(true);
	}
}
