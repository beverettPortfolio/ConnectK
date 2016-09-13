package test;

import com.beverett.derpai.BoardEvaluator;

import junit.framework.TestSuite;

/**
 * A test file that tests the evaluation function
 * @author Ben
 *
 */
public class EvaluatorTest extends AITest {

	public static TestSuite suite(){
		TestSuite testSuite=new TestSuite();
		testSuite.addTest(new EvaluatorTest("Empty Board", new byte[][]{{0,0,0},{0,0,0},{0,0,0}}, 3, false, 0));
		testSuite.addTest(new EvaluatorTest("Center", new byte[][]{{0,0,0},{0,1,0},{0,0,0}}, 3, false, 4));
		testSuite.addTest(new EvaluatorTest("1 Corner 2 Center", new byte[][]{{1,0,0},{0,2,0},{0,0,0}}, 3, false, -1));
		testSuite.addTest(new EvaluatorTest("Empty gravity", new byte[][]{{0,0,0},{0,0,0},{0,0,0}}, 3, true, 0));
		testSuite.addTest(new EvaluatorTest("Center gravity", new byte[][]{{0,0,0},{0,0,1},{0,0,0}}, 3, true, 20));
		testSuite.addTest(new EvaluatorTest("Corner gravity", new byte[][]{{1,0,0},{0,0,0},{0,0,0}}, 3, true, 30));
		testSuite.addTest(new EvaluatorTest("Center gravity", new byte[][]{{0,1,0},{0,1,0},{0,0,0}}, 3, true, 41));
		testSuite.addTest(new EvaluatorTest("Center gravity", new byte[][]{{1,1,0},{1,1,0},{0,0,0}}, 3, true, 63));
		return testSuite;
	}

	int expected;
	
	public EvaluatorTest(String name, byte[][] state, int kLength, boolean gravity, int expected) {
		super(name, state, kLength, gravity);
		this.expected=expected;
	}

	@Override
	protected void runTest() throws Throwable {
		assertEquals(expected,BoardEvaluator.evaluate(state, (byte) 1));
	}

}
