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
	private ArrayList<GridButton> player1Grid;
	private ArrayList<GridButton> player2Grid;
	private String player1;
	private String player2;
	private ArrayList<Ship> player1Ships;
	private ArrayList<Ship> player2Ships;
	private int playAgain;
	private boolean player1PlayAgain;
	private boolean player2PlayAgain;
	/**
	 * @param username
	 */
	public PlayerGrid(String player1, String player2) {
		this.player1 = player1;
		this.player2 = player2;
		this.startNewGame();
	}
	
	public void startNewGame() {
		player1Ships = new ArrayList<>();
		player2Ships = new ArrayList<>();
		player1Grid = new ArrayList<>();
		player2Grid = new ArrayList<>();
		this.makeGrid(player1Grid);
		this.makeGrid(player2Grid);
		this.makeShips(player1Ships, player1Grid);
		this.makeShips(player2Ships, player2Grid);
		this.playAgain = 0;
		player1PlayAgain = false;
		player2PlayAgain = false;
	}
	
	private void makeGrid(ArrayList<GridButton> grid) {
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
	private void makeShips(ArrayList<Ship> ships, ArrayList<GridButton> grid) {

		//place each ship on the grid
		boolean shipPlaced = false;

		while (!shipPlaced) {
			shipPlaced = placeShips(5, "Aircraft Carrier", ships, grid);
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(4, "Battleship", ships, grid);
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(3, "Cruiser", ships, grid);
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(3, "Submarine", ships, grid);
		}
		shipPlaced = false;
		while (!shipPlaced) {
			shipPlaced = placeShips(2, "Destroyer", ships, grid);
		}

	}

	/**
	 * Places each ship on the grid randomly
	 * @param numberOfParts number of squares the ship occupies
	 * @param shipType type of ship being placed
	 * @return true if the ships is successfully made
	 */
	private boolean placeShips(int numberOfParts, String shipType, ArrayList<Ship> ships, ArrayList<GridButton> playerGrid) {
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
		for (GridButton grid : playerGrid) {
			if (grid.getX_location() == x_location && grid.getY_location() == y_location)
			{
				shipStart = playerGrid.indexOf(grid);
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
					GridButton currentButton = playerGrid.get(xShip);
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
					GridButton currentButton = playerGrid.get(xShip);
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
					GridButton currentButton = playerGrid.get(yShip);
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
					GridButton currentButton = playerGrid.get(yShip);
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
	public String getPlayer1Name() {
		return this.player1;
	}
	
	/**
	 * @return the username
	 */
	public String getPlayer2Name() {
		return this.player2;
	}

	/**
	 * @return the grid
	 */
	public ArrayList<GridButton> getPlayer1Grid() {
		return this.player1Grid;
	}
	
	/**
	 * @return the grid
	 */
	public ArrayList<GridButton> getPlayer2Grid() {
		return this.player2Grid;
	}

	/**
	 * @return the ships
	 */
	public ArrayList<Ship> getPlayer1Ships() {
		return this.player1Ships;
	}
	
	/**
	 * @return the ships
	 */
	public ArrayList<Ship> getPlayer2Ships() {
		return this.player2Ships;
	}

	@Override
	public synchronized void propertyChange(PropertyChangeEvent evt) {
		Attack attack = (Attack) evt.getNewValue();
		InputOutputHandler ioHandler = (InputOutputHandler) evt.getSource();

		if (attack.isGameOver()) {
			this.playAgain++;

			if (attack.getUsername().equals(this.player1)) {

				if (attack.getX() == 1) {
					this.player1PlayAgain = true;
				}
			} else {

				if (attack.getX() == 1) {
					this.player2PlayAgain = true;
				}
			}
			
			if (this.playAgain == 2) {

				if (this.player2PlayAgain && this.player1PlayAgain) {
					this.startNewGame();
					ioHandler.playAgain(this);
				} else {
					if (!this.player1PlayAgain) {
						ioHandler.closeSocket(this.player1);
					}
					
					if (!this.player2PlayAgain) {
						ioHandler.closeSocket(this.player2);
					}
				}
			}			

		} else {

			ArrayList<Ship> ships = this.player1Ships;

			if (attack.getUsername() == this.player1) 
				ships = this.player2Ships;

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

						break;
					}
				}

				if (ship.isDestroyed()) 
					destroyed++;
			}

			if (destroyed == ships.size()) {
				attack.gameOver();
			}

			ioHandler.sendToPlayer(attack);
			ioHandler.sendBackToOpponent(attack);
		}
	}
	
}
