package net.chesstango.search;


import net.chesstango.search.smart.alphabeta.*;
import net.chesstango.search.smart.alphabeta.filters.SetBestMovesTest;
import net.chesstango.search.smart.alphabeta.filters.TranspositionEntryTableTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        AlphaBetaSymmetryTest.class,
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

        BestMovesBlackTest.class,
        BestMovesWhiteTest.class,

        TranspositionEntryTableTest.class,
        SetBestMovesTest.class
})
public class AlphaBetaTestSuite {

}
