package net.codingworks.sudoku;

import java.util.TimerTask;

/**
 * Stopper is the subclass of TimerTask for stopping a task.
 * 
 * @author Rongqin Sheng
 * @version 1.0
 * @see java.util.Timer
 */
public class Stopper extends TimerTask {

	/**
	 * A Stoppable object
	 */
	private Stoppable task;

	/**
	 * Constructor
	 * 
	 * @param task
	 *            a Stoppable object
	 */
	public Stopper(Stoppable task) {
		this.task = task;
	}

	/**
	 * Stop a task.
	 */
	public void run() {
		task.stop();
	}
}
