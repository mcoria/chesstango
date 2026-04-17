package net.chesstango.search;

import net.chesstango.search.smart.IterativeDeepeningTest;
import net.chesstango.search.smart.alphabeta.*;
import net.chesstango.search.smart.alphabeta.core.filters.TranspositionEntryTableTest;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTranspositionTest;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTriangularTest;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollectionTest;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationComparatorTest;
import net.chesstango.search.smart.alphabeta.root.filters.AspirationWindowsTest;
import net.chesstango.search.smart.alphabeta.root.filters.RootMoveEvaluationTrackerTest;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntryTest;
import net.chesstango.search.visitors.ChainPrinterVisitorTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        DetectCycleEnabledTest.class,
        DetectCycleDisabledTest.class,

        AlphaBetaMateIn1Test.class,
        AlphaBetaMateIn2Test.class,
        AlphaBetaMateIn3Test.class,
        AlphaBetaMateIn4Test.class,

        AlphaBetaTest.class,
        AlphaBetaStopTest.class,

        BestMovesBlackTest.class,
        BestMovesWhiteTest.class,

        TranspositionEntryTest.class,
        TranspositionEntryTableTest.class,

        PVCalculatorTranspositionTest.class,
        PVCalculatorTriangularTest.class,

        RootMoveEvaluationComparatorTest.class,
        RootMoveEvaluationCollectionTest.class,
        RootMoveEvaluationTrackerTest.class,

        IterativeDeepeningTest.class,
        AspirationWindowsTest.class,

        AlphaBetaStatisticsTest.class,

        SearchTest.class,
        SymmetryTest.class,
        RepetitionTest.class,

        ChainPrinterVisitorTest.class
})
public class SmartTestSuite {
}
