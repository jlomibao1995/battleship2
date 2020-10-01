package problemdomain;

import java.util.ArrayList;

public class Ship {
	
	private int size;
	ArrayList<GridButton> shipParts;
	String shipType;
	
	public Ship(int size, String shipType)
	{
		this.size = size;
		this.shipType = shipType;
		shipParts = new ArrayList<GridButton>();
	}
	
	public void addShipPart(GridButton shipPart) {
		shipParts.add(shipPart);
	}
	
	public String isDestroyed()
	{
		int counter = 1;
		for (GridButton shipPart : shipParts)
		{
			if (shipPart.isHit())
			{
				counter++;
			}
			System.out.println(counter);
		}
		
		if (counter == size)
		{
			return String.format("%s %s", shipType, "has been sunk!");
		}
		else 
			return "A ship has been hit";
	}

}
