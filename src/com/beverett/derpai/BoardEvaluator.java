package com.beverett.derpai;

import java.awt.Point;
import java.util.HashMap;

import connectK.BoardModel;

/**
 * A class that evaluates board states
 * 
 * @author Ben
 *
 */
public class BoardEvaluator {
	private static enum Heuristics {
		P1LINES, P2LINES, P1EVEN, P1ODD, P2EVEN, P2ODD
	}

	/**
	 * The evaluation function for the MiniMax search. The Heuristics are:
	 * Winning lines: A line of k spaces in any direction that contains at least
	 * one allied piece and no enemy pieces Even threats: A winning line with
	 * k-1 allied pieces where the empty space is on an even row Odd threats: A
	 * winning line with k-1 allied pieces where the empty space is on an odd
	 * row Weights for the heuristics are set at the end When gravity is off,
	 * even and odd threats are ignored.
	 * 
	 * @param searchState
	 *            The BoardModel to be evaluated
	 * @return An int representing how favorable the board state is. Higher
	 *         numbers are more favorable for the active player
	 */
	public static int evaluate(BoardModel searchState, byte player) {
		int p1Lines = 0;
		int p2Lines = 0;
		int p1Even = 0;
		int p2Even = 0;
		int p1Odd = 0;
		int p2Odd = 0;
		byte winner = searchState.winner();
		// No winner, evaluate
		if (winner == -1) {
			int kLength = searchState.getkLength();
			// Searches every space for winning lines and threats that start on
			// that space
			for (int i = 0; i < searchState.getWidth(); ++i) {
				for (int j = 0; j < searchState.getHeight(); ++j) {
					// Horizontal
					HashMap<Heuristics, Integer> horizontalRows = searchRow(searchState, i, j, kLength, 1, 0);
					p1Lines += horizontalRows.get(Heuristics.P1LINES);
					p2Lines += horizontalRows.get(Heuristics.P2LINES);
					p1Even += horizontalRows.get(Heuristics.P1EVEN);
					p1Odd += horizontalRows.get(Heuristics.P1ODD);
					p2Even += horizontalRows.get(Heuristics.P2EVEN);
					p2Odd += horizontalRows.get(Heuristics.P2ODD);

					// Vertical. Threats are not counted for vertical winning
					// lines
					HashMap<Heuristics, Integer> verticalRows = searchRow(searchState, i, j, kLength, 0, 1);
					p1Lines += verticalRows.get(Heuristics.P1LINES);
					p2Lines += verticalRows.get(Heuristics.P2LINES);

					// Diagonal up and to the right
					HashMap<Heuristics, Integer> upRightRows = searchRow(searchState, i, j, kLength, 1, 1);
					p1Lines += upRightRows.get(Heuristics.P1LINES);
					p2Lines += upRightRows.get(Heuristics.P2LINES);
					p1Even += upRightRows.get(Heuristics.P1EVEN);
					p1Odd += upRightRows.get(Heuristics.P1ODD);
					p2Even += upRightRows.get(Heuristics.P2EVEN);
					p2Odd += upRightRows.get(Heuristics.P2ODD);

					// Diagonal up and to the left
					HashMap<Heuristics, Integer> upLeftRows = searchRow(searchState, i, j, kLength, -1, 1);
					p1Lines += upLeftRows.get(Heuristics.P1LINES);
					p2Lines += upLeftRows.get(Heuristics.P2LINES);
					p1Even += upLeftRows.get(Heuristics.P1EVEN);
					p1Odd += upLeftRows.get(Heuristics.P1ODD);
					p2Even += upLeftRows.get(Heuristics.P2EVEN);
					p2Odd += upLeftRows.get(Heuristics.P2ODD);
				}
			}
			// set weights here
			int evaluationFunction;
			// gravity on, weight even and odd threats
			if (searchState.gravityEnabled()) {
				evaluationFunction = (10 * p1Lines) - (10 * p2Lines) + (p1Even) + (p1Odd) - (p2Even) - (p2Odd);
			}
			// gravity off, only check winning lines
			else {
				evaluationFunction = (p1Lines) - (p2Lines);
			}
			if (player == 1) {
				return evaluationFunction;
			} else {
				return -evaluationFunction;
			}
		}
		// If Either player wins return Infinity or -Infinity
		else if (winner == player) {
			return Integer.MAX_VALUE;
		}
		// Ties are 0
		else if (winner == 0) {
			return 0;
		} else {
			return Integer.MIN_VALUE;
		}
	}

	/**
	 * Searches a line of k spaces to check for winning lines.
	 * 
	 * @param searchState
	 *            The board state to be searched
	 * @param row
	 *            The row to start on
	 * @param column
	 *            The column to start on
	 * @param k
	 *            The amount needed in a row to win
	 * @param horizontalMovement
	 *            What direction to move horizontally. 1 for right, 0 for
	 *            vertical only, -1 for left
	 * @param verticalMovement
	 *            What direction to move vertically. 1 for up, 0 for horizontal
	 *            only. Downward movement is not needed.
	 * @return A hashmap containing the Heuristics
	 */
	private static HashMap<Heuristics, Integer> searchRow(BoardModel searchState, int row, int column, int k, int horizontalMovement, int verticalMovement) {
		int countK = 0;
		int count1 = 0;
		int count2 = 0;
		int count0 = 0;
		HashMap<Heuristics, Integer> values = new HashMap<Heuristics, Integer>();
		for (Heuristics h : Heuristics.values()) {
			values.put(h, 0);
		}
		Point threat = null;
		// Make sure the line is within the board's boundaries
		if (row + (k * horizontalMovement) >= -1 && row + (k * horizontalMovement) <= searchState.getWidth() && column + (k * verticalMovement) <= searchState.getHeight()) {
			while ((count1 == 0 || count2 == 0) && countK < k) {
				switch (searchState.getSpace(row + (horizontalMovement * countK), column + (verticalMovement * countK))) {
				case 0:
					count0++;
					threat = new Point(row, column + countK);
					break;
				case 1:
					count1++;
					break;
				case 2:
					count2++;
					break;
				}
				countK++;
			}
			// Winning line for p1
			if (count1 > 0 && count2 == 0) {
				values.put(Heuristics.P1LINES, values.get(Heuristics.P1LINES) + 1);
				// Threats are checked for lines with horizontal movement when
				// gravity is on
				if (horizontalMovement != 0 && searchState.gravityEnabled() && count1 == k - 1 && count0 == 1) {
					if (threat.getY() % 2 == 0) {
						values.put(Heuristics.P1EVEN, values.get(Heuristics.P1EVEN) + 1);
					} else {
						values.put(Heuristics.P1ODD, values.get(Heuristics.P1ODD) + 1);
					}
				}
			}
			// Winning line for p2
			else if (count2 > 0 && count1 == 0) {
				values.put(Heuristics.P2LINES, values.get(Heuristics.P2LINES) + 1);
				// Threats are checked for lines with horizontal movement when
				// gravity is on
				if (horizontalMovement != 0 && searchState.gravityEnabled() && count2 == k - 1 && count0 == 1) {
					if (threat.getY() % 2 == 0) {
						values.put(Heuristics.P2EVEN, values.get(Heuristics.P2EVEN) + 1);
					} else {
						values.put(Heuristics.P2ODD, values.get(Heuristics.P2ODD) + 1);
					}
				}
			}
		}
		return values;
	}
}
