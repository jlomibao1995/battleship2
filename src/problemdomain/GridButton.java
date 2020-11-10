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
	private int x_location;
	private int y_location;
	private boolean shipPart;
	private boolean hit;	
	private JButton button;

	/**
	 * Constructor for the class.
	 * @param x_location x-coordinate
	 * @param y_location y-coordinate
	 */
	public GridButton(int x_location, int y_location) {
		this.x_location = x_location;
		this.y_location = y_location;
		this.hit = false;
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
	
	public void makeShipPart() {
		this.shipPart = true;
	}

	/**
	 * @return the button
	 */
	public JButton getButton() {
		return button;
	}

	/**
	 * @param button the button to set
	 */
	public void setButton(JButton button) {
		this.button = button;
	}
	
	public void hit() {
		this.hit = true;
	}
}

