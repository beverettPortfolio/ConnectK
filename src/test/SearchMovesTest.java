package test;

import java.awt.Point;

import com.beverett.derpai.Action;
import com.beverett.derpai.MoveDecider;

import junit.framework.TestSuite;

/**
 * A test file that tests the searchMoves function
 * 
 * @author Ben
 *
 */
public class SearchMovesTest extends AITest {
	public static TestSuite suite() {
		TestSuite testSuite = new TestSuite();
		testSuite.addTest(new SearchMovesTest("Empty Board, depth 0", new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, 3, false, 0, new Action(new Point(), 0)));
		testSuite.addTest(new SearchMovesTest("Empty Board, depth 1", new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, 3, false, 1, new Action(new Point(1, 1), 40)));
		testSuite.addTest(new SearchMovesTest("Error State", new byte[][] { { 2, 1, 0 }, { 0, 1, 0 }, { 0, 2, 2 } }, 3, false, 20, new Action(new Point(2, 0), 0)));
		testSuite.addTest(new SearchMovesTest("Empty Board, depth infinite", new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }, 3, false, 20, new Action(new Point(0, 0), 0)));
		testSuite.addTest(new SearchMovesTest("Possible Error State 7,6,4", new byte[][] { { 1, 2, 1, 2, 1, 0 }, { 2, 0, 0, 0, 0, 0 }, { 2, 2, 2, 1, 2, 1 }, { 1, 1, 2, 1, 1, 1 }, { 0, 0, 0, 0, 0, 0 }, { 1, 2, 1, 2, 1, 0 }, { 2, 1, 2, 2, 2, 0 } }, 4, true, 20, new Action(new Point(0, 5), Integer.MIN_VALUE)));
		return testSuite;
	}

	private Action expected;
	private int startingDepth;

	public SearchMovesTest(String name, byte[][] state, int kLength, boolean gravity, int startingDepth, Action expected) {
		super(name, state, kLength, gravity);
		this.expected = expected;
		this.startingDepth = startingDepth;
	}

	@Override
	protected void runTest() throws Throwable {
		MoveDecider d = new MoveDecider(null, (byte) 1, startingDepth);
		System.out.println(state);
		Action actual = d.searchMoves(state, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, (byte) 1);
		System.out.println(actual);
		assertTrue(expected.equals(actual));
	}
}
