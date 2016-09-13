package com.beverett.derpai;


import java.util.TimerTask;

/**
 * @author Ben
 * A TimerTask for timing the thread that MoveDecider runs on
 */
public class MoveTimer extends TimerTask{
	Thread toInterrupt;
	
	/**
	 * Class constructor
	 * @param toInterrupt The thread to interrupt
	 */
	public MoveTimer(Thread toInterrupt) {
		super();
		this.toInterrupt = toInterrupt;
	}

	@Override
	public void run() {
		toInterrupt.interrupt();
	}
	
}
