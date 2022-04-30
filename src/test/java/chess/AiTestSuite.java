/**
 * 
 */
package chess;

import chess.ai.BestMoverFinderSuiteTest;
import chess.ai.imp.smart.CheckMateInOneTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ BestMoverFinderSuiteTest.class, CheckMateInOneTest.class})
public class AiTestSuite {

}
