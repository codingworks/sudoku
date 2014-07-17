package net.codingworks.sudoku;

/**
 * Based on the solver implementation, this class uses a similar backtracking
 * algorithm to determine one of 3 properties for a Sudoku puzzle: (1) it has a
 * unique solution, (2) it has multiple solutions, (3) it has no solution.
 * 
 * @author Rongqin Sheng
 * @version 1.0
 */
public class Analyzer implements Stoppable {

	/**
	 * An input Board object
	 */
	private Board input;

	/**
	 * An solution counter
	 */
	private int solutionCounter = 0;

	/**
	 * An array to store the first 2 solutions
	 */
	private Board[] output = new Board[2];

	/**
	 * Flag to stop the analyzer
	 */
	private boolean stopped = false;

	/**
	 * Constructor
	 * 
	 * @param input
	 *            an input Board object
	 */
	public Analyzer(Board input) {
		this.input = input;
	}

	/**
	 * Analyze the puzzle recursively
	 * 
	 * @param input
	 *            input Board object
	 * @param numberOfEmptyCells
	 *            number of empty cells
	 * @return a Board object (a solution or null)
	 */
	private Board analyze(Board input, int numberOfEmptyCells) {
		if (stopped) {
			return null;
		}
		Board ret = null;
		if (numberOfEmptyCells == 0) {
			if (solutionCounter < 2) {
				output[solutionCounter] = input;
			}
			solutionCounter++;
			return input;
		}
		int[] idx = Solver.selectCell(input);
		if (idx == null)
			return input;
		Cell cell = input.getCell(idx);

		for (byte v = 1; v <= 9; v++) {
			if (cell.getAvailable(v)) {
				Board board = input.copy(false);
				if (Solver.updateBoard(board, idx, v)) {
					Board result = analyze(board, numberOfEmptyCells - 1);
					if (result != null) {
						if (numberOfEmptyCells == input.getNumberOfEmptyCells()
								&& solutionCounter > 1) {
							return result;
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Function called by a different thread to stop the analyzer.
	 */
	public synchronized void stop() {
		stopped = true;
	}

	/**
	 * Check if the solver is stopped.
	 * 
	 * @return true for stopped and false otherwise
	 */
	public boolean stopped() {
		return stopped;
	}

	/**
	 * Get the report
	 * 
	 * @return the report
	 * @throws Exception
	 */
	public String getReport() throws Exception {
		input.updateCells();
		analyze(input, input.getNumberOfEmptyCells());
		String ret = "No solution";
		if (solutionCounter == 1) {
			ret = "Unique solution\n" + output[0].toString();
		} else if (solutionCounter > 1) {
			ret = "Multiple solutions (show 2 of them)\n"
					+ output[0].toString() + "\n" + output[1].toString();
		}
		return ret;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err
					.println("Usage: java net.codingworks.sudoku.Analyzer <input string>");
			System.exit(1);
		}
		try {
			Board board = new Board(args[0]);
			Analyzer a = new Analyzer(board);
			System.out.println(a.getReport());
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}
