package com.beverett.derpai;


import java.awt.Point;

/**
 * A POJO that contains a move and its corresponding value
 * @author Ben
 * 
 */
public class Action {
	private Point move;
	private int value;
	
	
	/**
	 * Class constructor
	 * @param move A point on the board representing a move
	 * @param value The value of the board state resulting from that move
	 */
	public Action(Point move, int value) {
		this.move = move;
		this.value = value;
	}
	
	/**
	 * Getter for move
	 * @return The move contained by this object
	 */
	public Point getMove() {
		return move;
	}
	
	/**
	 * Setter for move
	 * @param move The move to store in this object
	 */
	public void setMove(Point move) {
		this.move = move;
	}
	
	/**
	 * Getter for value
	 * @return The value contained by this object
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Setter for value
	 * @param value The value to store in this object
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((move == null) ? 0 : move.hashCode());
		result = prime * result + value;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Action other = (Action) obj;
		if (move == null) {
			if (other.move != null)
				return false;
		} else if (!move.equals(other.move))
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Action [move=" + move + ", value=" + value + "]";
	}
	
	
}
