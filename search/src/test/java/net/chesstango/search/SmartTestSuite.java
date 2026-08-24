package net.chesstango.search;

import net.chesstango.search.smart.*;
import net.chesstango.search.smart.core.filters.QuiescenceStandingPatTest;
import net.chesstango.search.smart.core.filters.TranspositionEntryTableTest;
import net.chesstango.search.smart.pv.model.PVCalculatorTriangularTest;
import net.chesstango.search.smart.root.RootMoveEvaluationBestTest;
import net.chesstango.search.smart.root.filters.AspirationWindowsTest;
import net.chesstango.search.smart.root.filters.RootMoveEvaluationTrackerTest;
import net.chesstango.search.smart.transposition.TranspositionEntryTest;
import net.chesstango.search.visitors.ChainPrinterVisitorTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        BoundTest.class,

        QuiescenceStandingPatTest.class,

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

        PVCalculatorTriangularTest.class,

        RootMoveEvaluationBestTest.class,
        RootMoveEvaluationTrackerTest.class,
        RootMoveEvaluationBestTest.class,

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
