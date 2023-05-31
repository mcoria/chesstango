package net.chesstango.search;


import net.chesstango.search.smart.alphabeta.*;
import net.chesstango.search.smart.alphabeta.filters.SetBestMoveOptionsTest;
import net.chesstango.search.smart.alphabeta.filters.TranspositionTableTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        AlphaBetaStatisticsTest.class,

        DetectCycleEnabledTest.class,
        DetectCycleDisabledTest.class,
        AlphaBetaMateIn1Test.class,
        AlphaBetaMateIn2Test.class,
        AlphaBetaMateIn3Test.class,
        AlphaBetaMateIn4Test.class,

        AlphaBetaTest.class,
        AlphaBetaGenericTest.class,
        AlphaBetaStopTest.class,

        BlackBestMovesTest.class,
        WhiteBestMovesTest.class,

        TranspositionTableTest.class,
        SetBestMoveOptionsTest.class
})
public class AlphaBetaTestSuite {

}
