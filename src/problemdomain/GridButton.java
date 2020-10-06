package problemdomain;

import javax.swing.JButton;
import java.awt.*;
import java.io.Serializable;

/**
 * Class for the grid buttons and their coordinates
 * @author Jean
 * @version October 6, 2020
 *
 */
public class GridButton implements Serializable{
	private static final long serialVersionUID = 1L;
	private JButton button;
	private Integer x_location;
	private Integer y_location;
	private Boolean shipPart = new Boolean(false);
	private Boolean hit = new Boolean(false);	
	private Ship ship;

	/**
	 * Constructor for the class.
	 * @param button button on the grid
	 * @param x_location x-coordinate
	 * @param y_location y-coordinate
	 */
	public GridButton(JButton button, int x_location, int y_location) {
		this.button = button;
		this.x_location = new Integer(x_location);
		this.y_location = new Integer(y_location);
		button.setBackground(Color.BLUE);
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
	 * @return the ship
	 */
	public Ship getShip() {
		return ship;
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

	/**
	 * Makes the button a part of a ship.
	 * @param ship the ship the button is a part of
	 */
	public void makeShipPart(Ship ship)
	{
		this.ship = ship;
		this.shipPart = true;
		button.setBackground(Color.LIGHT_GRAY);
	}

	/**
	 * 
	 * @return true if it is a ship part
	 */
	public boolean isShipPart()
	{
		return this.shipPart;
	}

	/**
	 * 
	 * @return true if the button has been clicked
	 */
	public boolean isHit()
	{
		return hit;
	}

	/**
	 * Changes the color if the button has been clicked depending on if it's a ship or not.
	 * @return message if a ship has been hit or not
	 */
	public String clicked()
	{
		//set that the button has been hit
		this.hit = true;
		//check if button is part of a ship
		if (isShipPart())
		{
			//update the ship the button belongs to
			ship.updateShip();
			if (ship.isDestroyed()) {
				return String.format("%s %s", ship.shipType, "has been sunk!");
			}
			else {
				button.setBackground(Color.RED);
				return "A ship has been hit";
			}
		}
		else 
		{
			button.setBackground(Color.YELLOW);
			return "Missed!";
		}

	}

}

