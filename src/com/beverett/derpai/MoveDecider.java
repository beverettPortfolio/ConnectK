package com.beverett.derpai;

import java.awt.Point;
import java.util.ArrayList;

import connectK.BoardModel;

/**
 * @author Ben The class that implements the minimax search for finding the best
 *         move
 */
public class MoveDecider implements Runnable {
	private int currentDepth;
	private Action currentBestMove;
	private byte player;
	private BoardModel state;

	/**
	 * Class constructor
	 * 
	 * @param state
	 *            The BoardModel containing the search state
	 * @param player
	 *            The current active player
	 */
	public MoveDecider(BoardModel state, byte player) {
		currentDepth = 0;
		currentBestMove = new Action(new Point(-2, -2), 0);
		this.player = player;
		this.state = state;
	}

	/**
	 * Class constructor specifying a starting search depth. Used for unit
	 * tests.
	 * 
	 * @param state
	 *            The BoardModel containing the search state
	 * @param player
	 *            The current active player
	 * @param startingDepth
	 *            The starting search depth
	 */
	public MoveDecider(BoardModel state, byte player, int startingDepth) {
		this(state, player);
		currentDepth = startingDepth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public synchronized void run() {
		if (state.spacesLeft == state.getHeight() * state.getWidth()) {
			currentBestMove = new Action(new Point(state.getWidth() / 2, state.getHeight() / 2), 0);
			return;
		}
		while (true) {
			try {
				Action bestMove = searchMoves(state, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
				currentBestMove = bestMove;
				currentDepth++;
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	/**
	 * A getter function called after the thread is interrupted
	 * 
	 * @return The current best move found
	 */
	public Point getBestMove() {
		return currentBestMove.getMove();
	}

	/**
	 * Resets the object to prepare for the next search
	 * 
	 * @param searchState
	 *            The BoardModel containing the next search state
	 */
	public void reset(BoardModel searchState) {
		currentDepth = 0;
		currentBestMove = new Action(new Point(-2, -2), 0);
		state = searchState;
	}

	/**
	 * Searches for the best move using minimax search with alpha-beta pruning
	 * 
	 * @param searchState
	 *            The state to search
	 * @param depth
	 *            The current depth that the search is at
	 * @param parentAlpha
	 *            The alpha value of the parent for AB pruning. Max moves pass
	 *            alpha to min moves
	 * @param parentBeta
	 *            The beta value of the parent for AB pruning. Min moves pass
	 *            beta to Max moves
	 * @param currentPlayer
	 *            The player number for the player that is moving. For max
	 *            moves, player==currentPlayer
	 * @return An action containing the value of the best move
	 * @throws InterruptedException
	 *             Used to return once the search time is up
	 */
	public Action searchMoves(BoardModel searchState, int depth, int parentAlpha, int parentBeta, byte currentPlayer) throws InterruptedException {
		Action bestAction = null;
		int alpha = parentAlpha;
		int beta = parentBeta;
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		byte winner = searchState.winner();
		// Reached the depth limit, or the board is a winning state
		// Evaluate, return
		if (depth == currentDepth || winner != -1) {
			int score = BoardEvaluator.evaluate(searchState, player);
			bestAction = new Action(new Point(), score);
		}
		// for each possible move, recur
		else {
			if (searchState.hasMovesLeft()) {
				for (Point move : findPossibleMoves(searchState)) {
					if (Thread.interrupted()) {
						throw new InterruptedException();
					}
					if (move != null) {
						byte otherPlayer;
						if (currentPlayer == 1) {
							otherPlayer = 2;
						} else {
							otherPlayer = 1;
						}
						Action searchAction;
						// MAX move. Finds the move with the largest evaluated
						// score
						// MAX moves set and pass alpha
						if (player == currentPlayer) {
							searchAction = searchMoves(searchState.placePiece(move, currentPlayer), depth + 1, alpha, Integer.MAX_VALUE, otherPlayer);
							if (bestAction == null || searchAction.getValue() > bestAction.getValue()) {
								bestAction = new Action(move, searchAction.getValue());
								alpha = searchAction.getValue();
								if (alpha >= beta) {
									return bestAction;
								}
							}
						}
						// MIN move. Finds the move with the smallest evaluated
						// score
						// MIN moves set and pass beta
						else {
							searchAction = searchMoves(searchState.placePiece(move, currentPlayer), depth + 1, Integer.MIN_VALUE, beta, otherPlayer);
							if (bestAction == null || searchAction.getValue() < bestAction.getValue()) {
								bestAction = new Action(move, searchAction.getValue());
								beta = searchAction.getValue();
								if (alpha >= beta) {
									return bestAction;
								}
							}
						}
					}
				}
			}
			// No possible moves
			// Evaluate, return
			else {
				int score = BoardEvaluator.evaluate(searchState, player);
				return new Action(new Point(-1, -1), score);
			}
		}
		return bestAction;
	}

	/**
	 * Gets all possible moves for the current search state. If gravity is on,
	 * it searches the top of each row. If gravity is off, it searches every
	 * space.
	 * 
	 * @param searchState
	 *            The state to be searched
	 * @return An array of all possible moves
	 */
	public Point[] findPossibleMoves(BoardModel searchState) {
		// Gravity on
		if (searchState.gravityEnabled()) {
			Point[] moves = new Point[searchState.getWidth()];
			for (int i = 0; i < searchState.getWidth(); i++) {
				if (searchState.getSpace(i, searchState.getHeight() - 1) == 0) {
					moves[i] = new Point(i, searchState.getHeight() - 1);
				}
			}
			return moves;
		}
		// Gravity off
		else {
			ArrayList<Point> moves = new ArrayList<Point>();
			int width = searchState.getWidth();
			int height = searchState.getHeight();
			for (int i = 0; i < width; ++i) {
				for (int j = 0; j < height; ++j) {
					if (searchState.getSpace(i, j) == 0) {
						moves.add(new Point(i, j));
					}
				}
			}
			return moves.toArray(new Point[moves.size()]);
		}
	}
}
