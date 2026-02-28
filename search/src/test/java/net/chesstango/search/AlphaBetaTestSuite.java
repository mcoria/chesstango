package net.chesstango.search;


import net.chesstango.search.smart.alphabeta.*;
import net.chesstango.search.smart.alphabeta.core.filters.TranspositionEntryTableTest;
import net.chesstango.search.smart.alphabeta.core.filters.once.AspirationWindowsTest;
import net.chesstango.search.smart.alphabeta.core.filters.TTPVReaderTest;
import net.chesstango.search.smart.alphabeta.core.filters.once.MoveEvaluationTrackerTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        AlphaBetaSymmetryTest.class,

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
        AlphaBetaSearchesTest.class,
        AspirationWindowsTest.class,
        MoveEvaluationTrackerTest.class,

        TTPVReaderTest.class,
        AlphaBetaStatisticsTest.class
})
public class AlphaBetaTestSuite {

}
