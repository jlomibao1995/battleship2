package problemdomain;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private String username;
	private Date date;
	private Integer x;
	private Integer y;
	private Boolean turn;
	private Boolean win;
	private Boolean play;
	private Boolean sendGrid;
	private Boolean sendShip;
	public Message(String username, String message){
		
		this.username = username;
		this.message = message;
		this.date = new Date();
		this.turn = false;
		this.win = false;
		this.play = true;
		this.sendGrid = false;
		this.sendShip = false;
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
	 * @return the x
	 */
	public Integer getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(Integer x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public Integer getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(Integer y) {
		this.y = y;
	}

	/**
	 * @return the turn
	 */
	public Boolean getTurn() {
		return turn;
	}

	/**
	 * @return the win
	 */
	public Boolean getWin() {
		return win;
	}

	/**
	 * @param win the win to set
	 */
	public void setWin(Boolean win) {
		this.win = win;
	}

	/**
	 * @param turn the turn to set
	 */
	public void setTurn(Boolean turn) {
		this.turn = turn;
	}
	

	/**
	 * @return the playAgain
	 */
	public Boolean getPlay() {
		return play;
	}

	/**
	 * @param playAgain the playAgain to set
	 */
	public void setPlay(Boolean playAgain) {
		this.play = playAgain;
	}

	/**
	 * @return the sendGrid
	 */
	public Boolean getSendGrid() {
		return sendGrid;
	}

	/**
	 * @param sendGrid the sendGrid to set
	 */
	public void setSendGrid(Boolean sendGrid) {
		this.sendGrid = sendGrid;
	}

	/**
	 * @return the sendShip
	 */
	public Boolean getSendShip() {
		return sendShip;
	}

	/**
	 * @param sendShip the sendShip to set
	 */
	public void setSendShip(Boolean sendShip) {
		this.sendShip = sendShip;
	}

	public String toString() {
		
		SimpleDateFormat dateFormatted = new SimpleDateFormat("h:mm a");
		
		String formatted = String.format("[%s] %s: %s", dateFormatted.format(date), username, message);
		
		return formatted;
	}

}
