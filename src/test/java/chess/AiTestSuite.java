/**
 * 
 */
package chess;

import chess.ai.BestMoveFinderSuiteTest;
import chess.ai.imp.smart.*;
import chess.ai.imp.smart.evaluation.GameEvaluatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        MinMaxTest.class,
        MinMaxPruningTest.class,
        GameEvaluatorTest.class,
        MoveSorterTest.class,
        BlackBestMovesTest.class,
        IterativeDeepingTest.class,
        BestMoveFinderSuiteTest.class,
        MateIn1Test.class,
        MateIn2Test.class,
        MateIn3Test.class,
        MateIn4Test.class
})
public class AiTestSuite {

}
