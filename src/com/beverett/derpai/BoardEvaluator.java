package com.beverett.derpai;

import java.awt.Point;

import connectK.BoardModel;

public class BoardEvaluator {
	/*
	 * The evaluation function for the MiniMax search.
	 * The Heuristics are:
	 * Winning rows: A line of k spaces that contains at least one allied piece and no enemy pieces
	 * Even threats: A winning row with k-1 allied pieces where the empty space is on an even row
	 * Odd threats: A winning row with k-1 allied pieces where the empty space is on an odd row
	 * Weights for the heuristics are set at the end
	 * When gravity is off, even and odd threats are ignored.
	 * 
	 * This method is kinda huge, and should probably be in its own class
	 * 
	 * @param searchState The BoardModel to be evaluated
	 * @return An int representing how favorable the board state is. Higher numbers are more favorable for the active player.
	 */
	public static int evaluate(BoardModel searchState, byte player){
		int p1Rows=0;
		int p2Rows=0;
		int p1Even=0;
		int p2Even=0;
		int p1Odd=0;
		int p2Odd=0;
		byte winner = searchState.winner();
		if (winner==-1){
			int width=searchState.getWidth();
			int height=searchState.getHeight();
			int kLength=searchState.getkLength();
			boolean gravity = searchState.gravityEnabled();
			//No winner, evaluate
			for(int i=0; i<width; ++i){
				for(int j=0; j<height; ++j){
					//Searches every line of k spaces for winning rows and threats
					int countK=0;
					int count1 = 0;
					int count2 = 0;
					int count0 = 0;
					Point threat = null;
					//Horizontal
					if (width-i>=kLength){
						while ((count1==0||count2==0)&&countK<kLength){
							switch (searchState.getSpace(i+countK, j)){
							case 0: 
								count0++;
								threat=new Point(i+countK, j);
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
						if (count1>0&&count2==0){
							p1Rows++;
							if (gravity&&count1==kLength-1&&count0==1){
								if (threat.getY()%2==0){
									p1Even++;
								}
								else{
									p1Odd++;
								}
							}
						}
						else if (count2>0&&count1==0){
							p2Rows++;
							if (gravity&&count2==kLength-1&&count0==1){
								if (threat.getY()%2==0){
									p2Even++;
								}
								else{
									p2Odd++;
								}
							}
						}
					}
					countK=0;
					count1 = 0;
					count2 = 0;
					count0 = 0;
					//Vertical
					if (height-j>=kLength){
						while ((count1==0||count2==0)&&countK<kLength){
							switch (searchState.getSpace(i, j+countK)){
							case 0: 
								count0++;
								threat=new Point(i, j+countK);
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
						//Vertical doesn't need to check threats
						if (count1>0&&count2==0){
							p1Rows++;
						}
						else if (count2>0&&count1==0){
							p2Rows++;
						}
					}
					countK=0;
					count1 = 0;
					count2 = 0;
					count0 = 0;
					//Diagonal up and to the right
					if (height-j>=kLength&&width-i>=kLength){
						while ((count1==0||count2==0)&&countK<kLength){
							switch (searchState.getSpace(i+countK, j+countK)){
							case 0: 
								count0++;
								threat=new Point(i, j+countK);
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
						if (count1>0&&count2==0){
							p1Rows++;
							if (gravity&&count1==kLength-1&&count0==1){
								if (threat.getY()%2==0){
									p1Even++;
								}
								else{
									p1Odd++;
								}
							}
						}
						else if (count2>0&&count1==0){
							p2Rows++;
							if (gravity&&count2==kLength-1&&count0==1){
								if (threat.getY()%2==0){
									p2Even++;
								}
								else{
									p2Odd++;
								}
							}
						}
					}
					countK=0;
					count1 = 0;
					count2 = 0;
					count0 = 0;
					//Diagonal up and to the left
					if (height-j>=kLength&&kLength-i<=1){
						while ((count1==0||count2==0)&&countK<kLength){
							switch (searchState.getSpace(i-countK, j+countK)){
							case 0: 
								count0++;
								threat=new Point(i, j+countK);
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
						if (count1>0&&count2==0){
							p1Rows++;
							if (gravity&&count1==kLength-1&&count0==1){
								if (threat.getY()%2==0){
									p1Even++;
								}
								else{
									p1Odd++;
								}
							}
						}
						else if (count2>0&&count1==0){
							p2Rows++;
							if (gravity&&count2==kLength-1&&count0==1){
								if (threat.getY()%2==0){
									p2Even++;
								}
								else{
									p2Odd++;
								}
							}
						}
					}
				}
			}
			//set weights here
			int evaluationFunction;
			//gravity on, weight even and odd threats
			if (searchState.gravityEnabled()){
				evaluationFunction=(10*p1Rows)-(10*p2Rows)+(p1Even)+(p1Odd)-(p2Even)-(p2Odd);
			}
			//gravity off, only check winning rows
			else{
				evaluationFunction=(p1Rows)-(p2Rows);
			}
			if (player==1){
				return evaluationFunction;
			}
			else{
				return -evaluationFunction;
			}
		}
		//If Either player wins return Infinity or -Infinity
		else if (winner==player){
			return Integer.MAX_VALUE;
		}
		else if (winner==0){
			return 0;
		}
		else{
			return Integer.MIN_VALUE;
		}
	}
}
