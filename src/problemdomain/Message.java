package problemdomain;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Stores a message and other information about the game
 * @author Jean
 *@version October 6, 2020
 *
 */
public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String message; //message to be sent
	private String username; //name of messenger
	private Date date; //date message was created
	
	//location of where a player attacked
	private Integer x; 
	private Integer y;
	
	private Boolean turn;//determines if it is the player's turn
	private Boolean win; //determines if a player has won
	private Boolean playAgain; //true if the player wan't to play again
	
	/**
	 * User defined constructor for the class.
	 * @param username username of who created the message
	 * @param message the message being sent
	 */
	public Message(String username, String message){
		
		this.username = username;
		this.message = message;
		this.date = new Date();
		this.turn = false;
		this.win = false;
		this.playAgain = false;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * @return the x-coordinate
	 */
	public Integer getX() {
		return x;
	}

	/**
	 * @param x the x-coordinate to set
	 */
	public void setX(Integer x) {
		this.x = x;
	}

	/**
	 * @return the y-coordiante
	 */
	public Integer getY() {
		return y;
	}

	/**
	 * @param y the y-coordinate to set
	 */
	public void setY(Integer y) {
		this.y = y;
	}

	/**
	 * @return turn true if it is the player's turn
	 */
	public Boolean getTurn() {
		return turn;
	}

	/**
	 * @return true if a player has won
	 */
	public Boolean getWin() {
		return win;
	}

	/**
	 * @param win set true if the player has won
	 */
	public void setWin(Boolean win) {
		this.win = win;
	}

	/**
	 * @param turn true or false depending on if it is the player's turn first
	 */
	public void setTurn(Boolean turn) {
		this.turn = turn;
	}
	

	/**
	 * @return true if the player want's to play again
	 */
	public Boolean getPlayAgain() {
		return playAgain;
	}

	/**
	 * @param playAgain true or false if player want's to play again
	 */
	public void setPlayAgain(Boolean playAgain) {
		this.playAgain = playAgain;
	}

	/**
	 * Formats message.
	 */
	public String toString() {
		
		SimpleDateFormat dateFormatted = new SimpleDateFormat("h:mm a");
		
		String formatted = String.format("[%s] %s: %s", dateFormatted.format(date), username, message);
		
		return formatted;
	}

}
