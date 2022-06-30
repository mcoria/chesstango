/**
 * 
 */
package net.chesstango;

import net.chesstango.ai.BestMoveFinderSuiteTest;
import net.chesstango.ai.imp.smart.*;
import net.chesstango.ai.imp.smart.evaluation.GameEvaluatorTest;
import net.chesstango.ai.imp.smart.evaluation.imp.GameEvaluatorImp01Test;
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
        GameEvaluatorImp01Test.class,
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
