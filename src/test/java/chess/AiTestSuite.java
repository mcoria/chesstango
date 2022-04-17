/**
 * 
 */
package chess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.ai.BestMoverFinderSuiteTest;
import chess.ai.imp.smart.CheckMateInOneTest;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ BestMoverFinderSuiteTest.class, CheckMateInOneTest.class})
public class AiTestSuite {

}
