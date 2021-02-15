/**
 * 
 */
package problemdomain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Jean
 *
 */
public class Attack implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private String username;
	private boolean hit;
	private Ship ship;
	private boolean gameOver;
	/**
	 * @param x
	 * @param y
	 * @param username
	 */
	public Attack(int x, int y, String username) {
		this.x = x;
		this.y = y;
		this.username = username;
		this.ship = null;
		this.hit = false;
		this.gameOver = false;
	}
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the ships
	 */
	public Ship getShips() {
		return ship;
	}
	/**
	 * @param ships the ships to set
	 */
	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
	public void hit() {
		this.hit = true;
	}
	/**
	 * @return the hit
	 */
	public boolean isHit() {
		return hit;
	}
	/**
	 * @return the gameOver
	 */
	public boolean isGameOver() {
		return gameOver;
	}
	/**
	 * @param gameOver the gameOver to set
	 */
	public void gameOver() {
		this.gameOver = true;;
	}
}
