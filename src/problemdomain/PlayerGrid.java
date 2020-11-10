package problemdomain;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;

import server.InputOutputHandler;

public class PlayerGrid implements Serializable, PropertyChangeListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<GridButton> grid;
	private String username;
	private ArrayList<Ship> ships;
	/**
	 * @param username
	 */
	public PlayerGrid(String username) {
		this.username = username;
		this.startNewGame();
	}
	
	public void startNewGame() {
		ships = new ArrayList<>();
		grid = new ArrayList<>();
		this.makeGrid();
		this.makeShips();
	}
	
	private void makeGrid() {
		for (int y= 1; y <= 10; y++)
		{

			for (int x = 1; x <= 10; x++)
			{
				GridButton gridButton = new GridButton(x, y);
				grid.add(gridButton);
			}
		}
	}
	

	/**
	 * Places all the ships needed for a standard battleship game on the grid.
	 */
	private void makeShips() {

		//place each ship on the grid
		boolean shipPlaced = false;

		while (!shipPlaced) {
			shipPlaced = placeShips(5, "Aircraft Carrier");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(4, "Battleship");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(3, "Cruiser");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(3, "Submarine");
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(2, "Destroyer");
		}

	}

	/**
	 * Places each ship on the grid randomly
	 * @param numberOfParts number of squares the ship occupies
	 * @param shipType type of ship being placed
	 * @return true if the ships is successfully made
	 */
	private boolean placeShips(int numberOfParts, String shipType) {
		ArrayList<GridButton> shipParts = new ArrayList<>();
		boolean horizontal = true;

		//get a number between 1 and 10 for the a and y coordinates
		int x_location = (int) (1 + Math.random() * 10);
		int y_location = (int) (1 + Math.random() * 10);

		//obtain a number to determine if the ship will be placed horizontally or vertically
		int num = (int) (1+ Math.random() *100);
		if (num % 2 == 0) {
			horizontal = false;
		}

		int shipStart = 0;

		//get the GridButton with the coordinates obtained
		for (GridButton grid : this.grid) {
			if (grid.getX_location() == x_location && grid.getY_location() == y_location)
			{
				shipStart = this.grid.indexOf(grid);
			}
		}

		//will determine if the ship can be placed on the grid depending if its occupied
		//if occupied the method returns false and the ship is not placed
		//ship is not fully placed until all parts have been placed in an unoccupied coordinate
		if (horizontal) {
			int xShip = shipStart;
			if (numberOfParts > x_location)
			{
				for (int i = 0; i < numberOfParts; i++)
				{

					xShip = shipStart + i;
					GridButton currentButton = this.grid.get(xShip);
					if (!currentButton.isShipPart()) {
						shipParts.add(currentButton);
					}
					else {
						return false;
					}
				}
			}
			else {

				for (int i = 0; i < numberOfParts; i++)
				{
					xShip = shipStart - i;
					GridButton currentButton = this.grid.get(xShip);
					if (!currentButton.isShipPart()) {
						shipParts.add(currentButton);
					}
					else {
						return false;
					}
				}
			}
		}
		else {
			int yShip = shipStart;
			if (numberOfParts > y_location)
			{
				for (int i = 0; i < numberOfParts; i++) {
					yShip = shipStart + i*10;
					GridButton currentButton = this.grid.get(yShip);
					if (!currentButton.isShipPart()) {
						shipParts.add(currentButton);
					}
					else {
						return false;
					}
				}
			}
			else {
				for (int i = 0; i < numberOfParts; i++) {
					yShip = shipStart - i*10;
					GridButton currentButton = this.grid.get(yShip);
					if (!currentButton.isShipPart()) {
						shipParts.add(currentButton);
					}
					else {
						return false;
					}
				}
			}
		}

		//create a ship object with the coordinates obtained
		Ship ship = new Ship(shipType);

		for (GridButton part: shipParts) {
			part.makeShipPart();
			ship.addShipPart(part);
		}

		ships.add(ship); //add ship to list of player ships

		return true;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the grid
	 */
	public ArrayList<GridButton> getGrid() {
		return grid;
	}

	/**
	 * @return the ships
	 */
	public ArrayList<Ship> getShips() {
		return ships;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Attack attack = (Attack) evt.getNewValue();
		int destroyed = 0;
		
		for (Ship ship: ships) {
			
			for (GridButton part: ship.getShipParts()) {
				int x = part.getX_location();
				int y = part.getY_location();
				
				if (x == attack.getX() && y == attack.getY()) {
					part.hit();
					ship.hit();
					attack.hit();
					
					if (ship.isDestroyed()) 
						attack.setShip(ship);
				}
				
				if (ship.isDestroyed()) 
					destroyed++;
			}
		}
		
		if (destroyed == ships.size()) {
			attack.gameOver();
		}
		
		InputOutputHandler ioHandler = (InputOutputHandler) evt.getSource();
		ioHandler.sendToPlayer(attack);
		ioHandler.sendBackToOpponent(attack);
	}
	
}
