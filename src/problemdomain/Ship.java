package problemdomain;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for the types of ships. Contains the list of buttons that make up the ship 
 * and the state of the ship whether it has been destroyed.
 * @author Jean
 * @version October 6, 2020
 *
 */
public class Ship implements Serializable {

	private static final long serialVersionUID = 2L;
	ArrayList<GridButton> shipParts; //list of buttons and coordinates that make up the ship
	String shipType; //name of the ship
	Boolean destroyed; //true if the ship has been destroyed
	
	/**
	 * User defined constructor for the class.
	 * @param shipType name of ship
	 */
	public Ship(String shipType)
	{
		this.shipType = shipType;
		this.shipParts = new ArrayList<GridButton>();
		this.destroyed = false;
	}

	/**
	 * Adds GridButton to the ship.
	 * @param shipPart GridButton containing the button and coordinates of the ship.
	 */
	public void addShipPart(GridButton shipPart) {
		shipParts.add(shipPart);
	}
	
	/**
	 * Updates the ship if it has been destroyed.
	 */
	public void updateShip()
	{
		//determine if all the parts have been hit
		int counter = 0;
		for (GridButton shipPart : shipParts)
		{
			if (shipPart.isHit())
			{
				counter++;
			}
		}
		
		if (counter == shipParts.size())
		{
			for (GridButton part: shipParts) {
				part.getButton().setBackground(Color.BLACK);
			}
			this.destroyed = true;
		}
	}
	
	/**
	 * 
	 * @return destroyed true if all the ship parts have been hit and destroyed
	 */
	public boolean isDestroyed() {
		return destroyed;
	}

}
