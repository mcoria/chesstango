/**
 * 
 */
package net.chesstango;

import net.chesstango.board.perft.*;
import net.chesstango.board.perft.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ InitialPositionTest.class, KiwipeteTest.class, PerftMainTestSuiteTest.class, PerftDifferentPositionsTest.class})
public class PerftTestSuite {
}
