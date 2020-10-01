package problemdomain;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
	
	private String message;
	private String username;
	private Date date;
	private Integer x;
	private Integer y;
	
	public Message(String username, String message){
		
		this.username = username;
		this.message = message;
		this.date = new Date();
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

	public String toString() {
		
		SimpleDateFormat dateFormatted = new SimpleDateFormat("h:mm a");
		
		String formatted = String.format("[%s] %s: %s", dateFormatted.format(date), username, message);
		
		return formatted;
	}

}
