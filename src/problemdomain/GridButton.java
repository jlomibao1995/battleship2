package problemdomain;

import javax.swing.JButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.Serializable;

public class GridButton implements Serializable {
	
	private JButton button;
	private int x_location;
	private int y_location;
	private boolean shipPart = false;
	private boolean hit = false;	
	private Ship ship;

	public GridButton(JButton button, int x_location, int y_location) {
		this.button = button;
		this.x_location = x_location;
		this.y_location = y_location;
	}

	/**
	 * @return the button
	 */
	public JButton getButton() {
		return button;
	}

	/**
	 * @return the x_location
	 */
	public int getX_location() {
		return x_location;
	}



	/**
	 * @param x_location the x_location to set
	 */
	public void setX_location(int x_location) {
		this.x_location = x_location;
	}


	/**
	 * @return the y_location
	 */
	public int getY_location() {
		return y_location;
	}


	/**
	 * @param y_location the y_location to set
	 */
	public void setY_location(int y_location) {
		this.y_location = y_location;
	}

	public void makeShipPart(Ship ship)
	{
		this.ship = ship;
		this.shipPart = true;
		button.setBackground(Color.BLUE);
	}
	
	public boolean isShipPart()
	{
		return this.shipPart;
	}
	
	public boolean isHit()
	{
		return hit;
	}
	
	public String clicked()
	{
		String message = "";
		if (isShipPart())
		{
			message = ship.isDestroyed();
			button.setBackground(Color.RED);
		}
		else 
		{
			button.setBackground(Color.YELLOW);
			message = "Missed!";
		}
		
		this.hit = true;
		
		return message;
	}

}

