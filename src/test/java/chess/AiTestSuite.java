/**
 * 
 */
package chess;

import chess.ai.BestMoverFinderSuiteTest;
import chess.ai.imp.smart.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GameEvaluatorTest.class,
        BlackBestMovesTest.class,
        SmartLoopTest.class,
        BestMoverFinderSuiteTest.class,
        MateIn1Test.class,
        MateIn2Test.class,
        MateIn3Test.class,
        MateIn4Test.class
})
public class AiTestSuite {

}
