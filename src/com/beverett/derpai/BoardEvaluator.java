package com.beverett.derpai;

import java.awt.Point;

import connectK.BoardModel;

/**
 * A class that evaluates board states
 * 
 * @author Ben
 *
 */
public class BoardEvaluator {

	//The weights for the evaluation function.
	private static final int WINNING_LINE_WEIGHT = 10;
	private static final int EVEN_THREAT_WEIGHT = 1;
	private static final int ODD_THREAT_WEIGHT = 1;
	
	/**
	 * The evaluation function for the MiniMax search.
	 * The Heuristics are:
	 * Winning lines: A line of k spaces in any direction that contains at least one allied piece and no enemy pieces 
	 * Even threats: A winning line with k-1 allied pieces where the empty space is on an even row 
	 * Odd threats: A winning line with k-1 allied pieces where the empty space is on an odd row 
	 * Weights for the heuristics are set at the end 
	 * When gravity is off, even and odd threats are ignored.
	 * 
	 * @param searchState
	 *            The BoardModel to be evaluated
	 * @return An int representing how favorable the board state is. Higher
	 *         numbers are more favorable for the active player
	 */
	public static int evaluate(BoardModel searchState, byte player) {
		int score = 0;
		byte winner = searchState.winner();
		// No winner, evaluate
		if (winner == -1) {
			int kLength = searchState.getkLength();
			// Searches every space for winning lines and threats that start on
			// that space
			for (int i = 0; i < searchState.getWidth(); ++i) {
				for (int j = 0; j < searchState.getHeight(); ++j) {
					// Horizontal
					score += searchRow(searchState, i, j, kLength, 1, 0);

					// Vertical. Threats are not counted for vertical winning
					// lines
					score += searchRow(searchState, i, j, kLength, 0, 1);

					// Diagonal up and to the right
					score += searchRow(searchState, i, j, kLength, 1, 1);

					// Diagonal up and to the left
					score += searchRow(searchState, i, j, kLength, -1, 1);
				}
			}
			if (player == 1) {
				return score;
			} else {
				return -score;
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
	private static int searchRow(BoardModel searchState, int row, int column, int k, int horizontalMovement, int verticalMovement) {
		int countK = 0;
		int count1 = 0;
		int count2 = 0;
		int count0 = 0;
		Point threat = null;
		int score = 0;
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
				score += WINNING_LINE_WEIGHT;
				// Threats are checked for lines with horizontal movement when
				// gravity is on
				if (horizontalMovement != 0 && searchState.gravityEnabled() && count1 == k - 1 && count0 == 1) {
					if (threat.getY() % 2 == 0) {
						score += EVEN_THREAT_WEIGHT;
					} else {
						score += ODD_THREAT_WEIGHT;
					}
				}
			}
			// Winning line for p2
			else if (count2 > 0 && count1 == 0) {
				score -= WINNING_LINE_WEIGHT;
				// Threats are checked for lines with horizontal movement when
				// gravity is on
				if (horizontalMovement != 0 && searchState.gravityEnabled() && count2 == k - 1 && count0 == 1) {
					if (threat.getY() % 2 == 0) {
						score -= EVEN_THREAT_WEIGHT;
					} else {
						score -= ODD_THREAT_WEIGHT;
					}
				}
			}
		}
		return score;
	}
}
