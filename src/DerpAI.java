
import java.awt.Point;
import java.util.Timer;

import com.beverett.derpai.MoveDecider;
import com.beverett.derpai.MoveTimer;

import connectK.BoardModel;
import connectK.CKPlayer;

/**
 * The main class for the AI. Extends the provided CKPlayer class.
 * 
 * @author Ben
 * 
 */
public class DerpAI extends CKPlayer {

	Timer timer;
	Thread thread;
	MoveDecider decider;

	/**
	 * Class Constructor
	 * 
	 * @param player
	 *            The player number
	 * @param state
	 *            The starting board state
	 */
	public DerpAI(byte player, BoardModel state) {
		super(player, state);
		teamName = "Derp";
		timer = new Timer();
		decider = new MoveDecider(null, this.player);
	}

	/**
	 * Gets the best move for the given board state Uses the default 5 second
	 * time out for returning a move
	 * 
	 * @param state
	 *            The BoardModel to get the next move for
	 */
	@Override
	public Point getMove(BoardModel state) {
		return getMove(state, 5000);
	}

	/**
	 * Gets the best move for the given board state Uses the specified time out
	 * for returning a move
	 * 
	 * @param state
	 *            The BoardModel to get the next move for
	 * @param deadline
	 *            The amount of time in milliseconds before the move times out
	 */
	@Override
	public Point getMove(BoardModel state, int deadline) {
		decider.reset(state);
		thread = new Thread(decider);
		timer.schedule(new MoveTimer(thread), deadline - 200);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return decider.getBestMove();
	}
}
