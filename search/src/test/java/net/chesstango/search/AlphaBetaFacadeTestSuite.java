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
        AlphaBetaFacadeStatisticsTest.class,

        DetectCycleEnabledTest.class,
        DetectCycleDisabledTest.class,
        AlphaBetaFacadeMateIn1Test.class,
        AlphaBetaFacadeMateIn2Test.class,
        AlphaBetaFacadeMateIn3Test.class,
        AlphaBetaFacadeMateIn4Test.class,

        AlphaBetaFacadeTest.class,
        AlphaBetaFacadeGenericTest.class,
        AlphaBetaFacadeStopTest.class,

        BlackBestMovesTest.class,
        WhiteBestMovesTest.class,

        TranspositionEntryTableTest.class,
        SetBestMovesTest.class
})
public class AlphaBetaFacadeTestSuite {

}
