package gui;

 import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;
 
public class ClientGUI {
	
	private JFrame frame;
	
	private JList chatList;
	private DefaultListModel chatListModel;
	
	private static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
	private static String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	
	public ClientGUI() {
		
		this.frame = new JFrame("Battleship");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(700, 800);
		this.frame.setLayout(new BorderLayout());
		
		JPanel gamePanel = new JPanel(new GridLayout(2, 1));
		gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10 ,40));
		
		JPanel clientPanel = createClientPanel();
		JPanel opponentPanel = createOpponentPanel();
		JPanel chatPanel = createChatPanel();
		
		gamePanel.add(clientPanel);
		gamePanel.add(opponentPanel);
		
		this.frame.add(gamePanel, BorderLayout.CENTER);
		this.frame.add(chatPanel, BorderLayout.EAST);
		display();
	}
	
	private JPanel createClientPanel()
	{
		JPanel centerPanel = new JPanel(new GridLayout(10,10));
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		JPanel letterPanel = new JPanel(new GridLayout(10, 1));
		JPanel numberPanel = new JPanel(new GridLayout(1, 10));
		
		for (int i = 0; i < 10; i++) {
			JLabel letter = new JLabel(letters[i], SwingConstants.CENTER);
			letterPanel.add(letter);
		}
		
		for (int i=0; i < 10; i++) {
			JLabel number = new JLabel(numbers[i], SwingConstants.CENTER);
			numberPanel.add(number);
		}
		
		JLabel title = new JLabel("Your Grid", SwingConstants.CENTER);
		
		for (int i= 0; i<100; i++)
		{
			JButton button = new JButton();
			//button.setPreferredSize(new Dimension(30, 40));
			button.setBackground(Color.BLUE);
			
			centerPanel.add(button);
		}
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(title, BorderLayout.NORTH);
		mainPanel.add(letterPanel, BorderLayout.WEST);
		mainPanel.add(numberPanel, BorderLayout.SOUTH);
		
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 30, 0));
		
		return mainPanel;
		
	}
	
	private JPanel createOpponentPanel()
	{
		JPanel centerPanel = new JPanel(new GridLayout(10,10));
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		JPanel letterPanel = new JPanel(new GridLayout(10, 1));
		JPanel numberPanel = new JPanel(new GridLayout(1, 10));
		
		for (int i = 0; i < 10; i++) {
			JLabel letter = new JLabel(letters[i], SwingConstants.CENTER);
			letterPanel.add(letter);
		}
		
		for (int i=0; i < 10; i++) {
			JLabel number = new JLabel(numbers[i], SwingConstants.CENTER);
			numberPanel.add(number);
		}
		
		JLabel title = new JLabel("Opponent's Grid", SwingConstants.CENTER);
		
		//mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		
		for (int i= 0; i<100; i++)
		{
			JButton button = new JButton();
			//button.setPreferredSize(new Dimension(30, 40));
			button.setBackground(Color.LIGHT_GRAY);
			button.setOpaque(true);
			
			centerPanel.add(button);
		}
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(title, BorderLayout.NORTH);
		mainPanel.add(letterPanel, BorderLayout.WEST);
		mainPanel.add(numberPanel, BorderLayout.SOUTH);
		
		return mainPanel;
		}
	
	private JPanel createChatPanel()
	{
		JPanel panel = new JPanel(new BorderLayout());
		JPanel typePanel = new JPanel(new BorderLayout());
		JButton sendButton = new JButton("Send");
		
		chatListModel = new DefaultListModel();
		chatList = new JList(chatListModel);
		JScrollPane scrollPane = new JScrollPane(chatList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JTextField inputMessage = new JTextField();
		Font font = new Font("SansSerif", Font.PLAIN, 15);
		inputMessage.setFont(font);
		
		typePanel.add(inputMessage, BorderLayout.CENTER);
		typePanel.add(sendButton, BorderLayout.SOUTH);
		
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(typePanel, BorderLayout.SOUTH);

		return panel;
	}
	
	public void display() {
		//this.frame.pack();
		this.frame.setVisible(true);
	}
}
