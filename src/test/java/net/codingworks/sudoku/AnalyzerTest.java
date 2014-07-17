package net.codingworks.sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for Analyzer
 * 
 * @author Rongqin Sheng
 * @version 1.0
 */
public class AnalyzerTest {

	@Test
	public void testMultipleSolutions() {
		try {
			String input = "000006000"
					     + "007089100"
					     + "000000056"
					     + "010700000"
					     + "000000300"
					     + "078460020"
					     + "600005000"
					     + "002000000"
					     + "804301070";
			Board board = new Board(input);
			Analyzer analyzer = new Analyzer(board);
			String report = analyzer.getReport();
			assertTrue(report.startsWith("Multiple solutions"));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testNoSolution() {
		try {
			String input = "840009002"
					     + "500000070"
					     + "007000800"
					     + "900030005"
					     + "000010730"
					     + "260005000"
					     + "000860000"
					     + "000500040"
					     + "008200509";
			Board board = new Board(input);
			Analyzer analyzer = new Analyzer(board);
			String report = analyzer.getReport();
			assertEquals("No solution", report);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testUniqueSolution() {
		try {
			String input = "009003060"
					     + "000040100"
					     + "500100000"
					     + "090000020"
					     + "800000400"
					     + "027006009"
					     + "000000000"
					     + "003002070"
					     + "000850600";
			Board board = new Board(input);
			Analyzer analyzer = new Analyzer(board);
			String report = analyzer.getReport();
			assertTrue(report.startsWith("Unique solution"));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
