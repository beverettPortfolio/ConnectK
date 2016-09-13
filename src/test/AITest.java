package test;

import java.awt.Point;

import connectK.BoardModel;
import junit.framework.TestCase;

public abstract class AITest extends TestCase{

	
	protected BoardModel state;

	public AITest(String name, byte[][] state, int kLength, boolean gravity) {
		super(name);
		this.state = createBoard(state, kLength, gravity);
	}

	private BoardModel createBoard(byte[][] boardState, int kLength, boolean gravity){
		BoardModel state=BoardModel.newBoard(boardState.length, boardState[0].length, kLength, gravity);
		for (int i=0;i<boardState.length;i++){
			for (int j=0;j<boardState[0].length; j++){
				if(boardState[i][j]!=0){
					state=state.placePiece(new Point(i,j), boardState[i][j]);
				}
			}
		}
		return state;
	}
	
}
