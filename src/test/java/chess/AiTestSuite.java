/**
 * 
 */
package chess;

import chess.ai.BestMoverFinderSuiteTest;
import chess.ai.imp.smart.GameEvaluatorTest;
import chess.ai.imp.smart.MateInOneTest;
import chess.ai.imp.smart.MateInTwoTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ BestMoverFinderSuiteTest.class, GameEvaluatorTest.class,
        MateInOneTest.class, MateInTwoTest.class})
public class AiTestSuite {

}
