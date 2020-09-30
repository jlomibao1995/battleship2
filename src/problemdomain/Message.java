package problemdomain;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
	
	private String message;
	private String username;
	private Date date;
	
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
	
	public String toString() {
		
		SimpleDateFormat dateFormatted = new SimpleDateFormat("h:mm a");
		
		String formatted = String.format("[%s] %s: %s", dateFormatted.format(date), username, message);
		
		return formatted;
	}

}
