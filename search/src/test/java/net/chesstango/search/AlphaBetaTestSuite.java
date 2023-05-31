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
        MinMaxPruningMateIn1Test.class,
        MinMaxPruningMateIn2Test.class,
        MinMaxPruningMateIn3Test.class,
        MinMaxPruningMateIn4Test.class,

        MinMaxPruningTest.class,
        MinMaxPruningGenericTest.class,
        MinMaxPruningStopTest.class,

        BlackBestMovesTest.class,
        WhiteBestMovesTest.class,

        TranspositionTableTest.class,
        SetBestMoveOptionsTest.class
})
public class AlphaBetaTestSuite {

}
