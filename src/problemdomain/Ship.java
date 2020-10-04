package problemdomain;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class Ship implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	ArrayList<GridButton> shipParts;
	String shipType;
	Boolean destroyed;
	
	public Ship(String shipType)
	{
		this.shipType = shipType;
		this.shipParts = new ArrayList<GridButton>();
		this.destroyed = false;
	}

	public void addShipPart(GridButton shipPart) {
		shipParts.add(shipPart);
	}
	
	public void updateShip()
	{
		int counter = 0;
		for (GridButton shipPart : shipParts)
		{
			if (shipPart.isHit())
			{
				counter++;
			}
			System.out.println(counter);
		}
		
		if (counter == shipParts.size())
		{
			for (GridButton part: shipParts) {
				part.getButton().setBackground(Color.BLACK);
			}
			this.destroyed = true;
		}
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}

}
