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
	
	/**
	 * User defined constructor for the class.
	 * @param username username of who created the message
	 * @param message the message being sent
	 */
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
	 * Formats message.
	 */
	public String toString() {
		
		SimpleDateFormat dateFormatted = new SimpleDateFormat("h:mm a");
		
		String formatted = String.format("[%s] %s: %s", dateFormatted.format(date), username, message);
		
		return formatted;
	}

}
