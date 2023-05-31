package net.chesstango;

import net.chesstango.board.perft.InitialPositionTest;
import net.chesstango.board.perft.KiwipeteTest;
import net.chesstango.board.perft.PerftDifferentPositionsTest;
import net.chesstango.board.perft.PerftMainTestSuiteTest;
import net.chesstango.board.perft.imp.PerftWithMapIterateDeepingTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({
        InitialPositionTest.class,
        KiwipeteTest.class,
        PerftMainTestSuiteTest.class,
        PerftDifferentPositionsTest.class,
        PerftWithMapIterateDeepingTest.class
})
public class PerftTestSuite {
}
