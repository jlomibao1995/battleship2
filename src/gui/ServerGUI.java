package gui;

import java.awt.*;

import javax.swing.*;

import problemdomain.Server;

public class ServerGUI {

	private JFrame frame;

	private JList trafficList;
	private DefaultListModel trafficListModel;

	private JList chatList;
	private DefaultListModel chatListModel;
	
	private JList gameList;
	private DefaultListModel gameListModel;

	public ServerGUI() {

		frame = new JFrame("BattleShip Server");
		JPanel monitor = new JPanel(new GridLayout(2, 1));

		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		this.frame.setSize(900, 600);

		JPanel trafficPanel = createTrafficPanel();
		JPanel chatPanel = createChatPanel();
		JPanel gamesPanel = createGamesPanel();

		monitor.add(chatPanel);
		monitor.add(gamesPanel);
		monitor.setBorder(BorderFactory.createEmptyBorder(10, 30, 10 ,10));
		trafficPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30 ,30));
		
		this.frame.add(trafficPanel, BorderLayout.CENTER);
		this.frame.add(monitor, BorderLayout.WEST);
		display();
		
		new Server();
	}

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

	public JPanel createChatPanel() {

		JPanel panel = new JPanel(new BorderLayout());
		JLabel title = new JLabel("Chat");

		chatListModel = new DefaultListModel();
		chatList = new JList(chatListModel);
		JScrollPane scrollPane = new JScrollPane(chatList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(title, BorderLayout.NORTH);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20 ,0));

		return panel;
	}
	
	public JPanel createGamesPanel() {

		JPanel panel = new JPanel(new BorderLayout());
		JLabel title = new JLabel("Games");
		
		gameListModel = new DefaultListModel();
		gameList = new JList(gameListModel);
		
		JScrollPane scrollPane = new JScrollPane(gameList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		panel.add(scrollPane);
		panel.add(title, BorderLayout.NORTH);

		return panel;
	}

	public void display() {
		//this.frame.pack();
		this.frame.setVisible(true);
	}
}
