package com.beverett.derpai;

import java.awt.Point;
import java.util.ArrayList;

import connectK.BoardModel;

/**
 * @author Ben
 * The class that implements the minimax search for finding the best move
 */
public class MoveDecider implements Runnable {
	private int currentDepth;
	private Action currentBestMove;
	private byte player;
	private BoardModel state;
	
	/**
	 * Class constructor
	 * 
	 * @param state The BoardModel containing the search state
	 * @param player The current active player
	 */
	public MoveDecider(BoardModel state, byte player){
		currentDepth=0;
		currentBestMove=new Action(new Point(-2,-2),0);
		this.player=player;
		this.state=state;
	}
	
	/**
	 * Class constructor specifying a starting search depth. Used for unit tests.
	 * 
	 * @param state The BoardModel containing the search state
	 * @param player The current active player
	 * @param startingDepth The starting search depth
	 */
	public MoveDecider(BoardModel state, byte player, int startingDepth){
		this(state, player);
		currentDepth=startingDepth;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public synchronized void run(){
		if (state.spacesLeft==state.getHeight()*state.getWidth()){
			currentBestMove = new Action(new Point(state.getWidth()/2, state.getHeight()/2),0);
			return;
		}
		while(true){
			try{
				Action bestMove=searchMoves(state, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
				//System.out.println(bestMove.getValue());
				currentBestMove=bestMove;
				currentDepth++;
			} catch(InterruptedException e){
				//System.out.println(currentBestMove.getValue());
				return;
			}
		}
	}
	
	/**
	 * A getter function called after the thread is interrupted
	 * 
	 * @return The current best move found
	 */
	public Point getBestMove(){
		//System.out.println(currentDepth);
		return currentBestMove.getMove();
	}
	
	/**
	 * Resets the object to prepare for the next search
	 * 
	 * @param searchState The BoardModel containing the next search state
	 */
	public void reset(BoardModel searchState){
		currentDepth=0;
		currentBestMove=new Action(new Point(-2,-2),0);
		state=searchState;
	}
	
	public Action searchMoves(BoardModel searchState, int depth, int parentAlpha, int parentBeta, byte currentPlayer) throws InterruptedException{
		Action bestAction=null;
		int alpha = parentAlpha;
		int beta = parentBeta;
		if (Thread.interrupted()){
			throw new InterruptedException();
		}
		byte winner = searchState.winner();
		if (depth==currentDepth||winner!=-1){
			//Evaluate, return
			int score = BoardEvaluator.evaluate(searchState, player);
			bestAction = new Action(new Point(), score);
		}
		else{
			//for each possible move, recur
			if(searchState.hasMovesLeft()){
				for (Point move: findPossibleMoves(searchState)){
					if (Thread.interrupted()){
						throw new InterruptedException();
					}
					if (move!=null){
						byte otherPlayer;
						if (currentPlayer==1){
							otherPlayer = 2;
						}
						else{
							otherPlayer = 1;
						}
						Action searchAction;
						if (player==currentPlayer){
							searchAction = searchMoves(searchState.placePiece(move, currentPlayer), depth+1, alpha, Integer.MAX_VALUE, otherPlayer);
							if (bestAction==null||searchAction.getValue()>bestAction.getValue()){
								bestAction=new Action(move, searchAction.getValue());
								alpha = searchAction.getValue();
								if(alpha>=beta){
									//System.out.println("Pruned at depth: "+depth+" with alpha: "+alpha+" and beta: "+beta);
									return bestAction;
								}
							}
						}
						else{
							searchAction = searchMoves(searchState.placePiece(move, currentPlayer), depth+1, Integer.MIN_VALUE, beta, otherPlayer);
							if (bestAction==null||searchAction.getValue()<bestAction.getValue()){
								bestAction=new Action(move, searchAction.getValue());
								beta = searchAction.getValue();
								if(alpha>=beta){
									//System.out.println("Pruned at depth: "+depth+" with alpha: "+alpha+" and beta: "+beta);
									return bestAction;
								}
							}
						}
					}
				}
			}
			else{
				int score = BoardEvaluator.evaluate(searchState, player);
				return new Action(new Point(-1,-1), score);
			}
		}
		return bestAction;
	}
	
	/*
	 * The max part for the minimax search 
	 * 
	 * @param searchState The state to search
	 * @param depth The current depth that the search is at
	 * @param parentBeta The beta value of the parent for AB pruning
	 * @return An action containing the value of the best move
	 * @throws InterruptedException Used to return once the search time is up
	 */
	public Action maxMove(BoardModel searchState, int depth, int parentBeta) throws InterruptedException{
		Action max=null;
		int alpha = Integer.MIN_VALUE;
		int beta = parentBeta;
		if (Thread.interrupted()){
			throw new InterruptedException();
		}
		byte winner = searchState.winner();
		if (depth==currentDepth||winner!=-1){
			//Evaluate, return
			max = new Action(new Point(), BoardEvaluator.evaluate(searchState, player));
		}
		else{
			//for each possible move, call minMove
			if(searchState.hasMovesLeft()){
				for (Point move: findPossibleMoves(searchState)){
					if (Thread.interrupted()){
						throw new InterruptedException();
					}
					if (move!=null){
						//System.out.println("move: "+move.toString()+" depth:"+depth);
						Action searchAction = minMove(searchState.placePiece(move, player), depth+1, alpha);
						if (max==null||searchAction.getValue()>max.getValue()){
							max=new Action(move, searchAction.getValue());
							//System.out.println(max.getValue());
							alpha=searchAction.getValue();
							if(alpha>=beta){
								//System.out.println("Pruned at depth: "+depth+" with alpha: "+alpha+" and beta: "+beta);
								return max;
							}
						}
					}
				}
			}
			else{
				return new Action(new Point(-1,-1), BoardEvaluator.evaluate(searchState, player));
			}
		}
		return max;
	}
	
	//Same thing as maxMove but for the other player
	public Action minMove(BoardModel searchState, int depth, int parentAlpha) throws InterruptedException{
		Action min=null;
		int alpha = parentAlpha;
		int beta = Integer.MAX_VALUE;
		if (Thread.interrupted()){
			throw new InterruptedException();
		}
		byte winner = searchState.winner();
		if (depth==currentDepth||winner!=-1){
			//Evaluate, return
			min = new Action(new Point(), BoardEvaluator.evaluate(searchState, player));
		}
		else{
			//for each possible move, call maxMove
			if (searchState.hasMovesLeft()){
				for (Point move: findPossibleMoves(searchState)){
					if (Thread.interrupted()){
						throw new InterruptedException();
					}
					if (move!=null){
						byte other;
						if (player==1){
							other=2;
						}
						else{
							other=1;
						}
						//System.out.println("move: "+move.toString()+" depth:"+depth);
						Action searchAction = maxMove(searchState.placePiece(move, other), depth+1, beta);
						if (min==null||searchAction.getValue()<min.getValue()){
							min=new Action(move, searchAction.getValue());
							beta = searchAction.getValue();
							if (alpha>=beta){
								//System.out.println("Pruned at depth: "+depth+" with alpha: "+alpha+" and beta: "+beta);
								return min;
							}
						}
					}
				}
			}
			else{
				return new Action(new Point(-1,-1), BoardEvaluator.evaluate(searchState, player));
			}
		}
		return min;
	}
	
	/*
	 * Gets all possible moves for the current search state.
	 * If gravity is on, it searches the top of each row.
	 * If gravity is off, it searches every space.
	 * 
	 * @param searchState The state to be searched
	 * @return An array of all possible moves
	 */
	public Point[] findPossibleMoves(BoardModel searchState){
		if (searchState.gravityEnabled()){
			Point[] moves = new Point[searchState.getWidth()];
			for (int i=0; i<searchState.getWidth();i++){
				if (searchState.getSpace(i, searchState.getHeight()-1)==0){
					moves[i]=new Point(i, searchState.getHeight()-1);
				}
			}
			return moves;
		}
		else{
			ArrayList<Point> moves = new ArrayList<Point>();
			int width=searchState.getWidth();
			int height=searchState.getHeight();
			for(int i=0; i<width; ++i){
				for(int j=0; j<height; ++j){
					if (searchState.getSpace(i, j)==0){
						moves.add(new Point(i,j));
					}
				}
			}
			return moves.toArray(new Point[moves.size()]);
		}
	}
}
