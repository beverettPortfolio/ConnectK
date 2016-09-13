package com.beverett.derpai;


import java.awt.Point;

/**
 * @author Ben
 * A POJO that contains an action and its corresponding value
 */
public class Action {
	private Point move;
	private int value;
	
	public Action(Point move, int value) {
		this.move = move;
		this.value = value;
	}
	
	public Point getMove() {
		return move;
	}
	public void setMove(Point move) {
		this.move = move;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((move == null) ? 0 : move.hashCode());
		result = prime * result + value;
		return result;
	}

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

	@Override
	public String toString() {
		return "Action [move=" + move + ", value=" + value + "]";
	}
	
	
}
