package net.chesstango.search;

import net.chesstango.search.alphabeta.*;
import net.chesstango.search.alphabeta.core.filters.QSStandingPatTest;
import net.chesstango.search.alphabeta.core.filters.TranspositionEntryTableTest;
import net.chesstango.search.alphabeta.pv.model.PVCalculatorTest;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationBestTest;
import net.chesstango.search.alphabeta.root.filters.AspirationWindowsTest;
import net.chesstango.search.alphabeta.root.filters.RootMoveEvaluationTrackerTest;
import net.chesstango.search.alphabeta.transposition.TTableArrayTest;
import net.chesstango.search.alphabeta.transposition.TranspositionEntryTest;
import net.chesstango.search.visitors.ChainPrinterVisitorTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        BoundTest.class,

        QSStandingPatTest.class,

        DetectCycleEnabledTest.class,
        DetectCycleDisabledTest.class,

        AlphaBetaMateIn1Test.class,
        AlphaBetaMateIn2Test.class,
        AlphaBetaMateIn3Test.class,
        AlphaBetaMateIn4Test.class,

        AlphaBetaStopTest.class,

        BestMovesBlackTest.class,
        BestMovesWhiteTest.class,

        TranspositionEntryTest.class,
        TranspositionEntryTableTest.class,

        PVCalculatorTest.class,

        RootMoveEvaluationBestTest.class,
        RootMoveEvaluationTrackerTest.class,
        RootMoveEvaluationBestTest.class,

        IterativeDeepeningTest.class,
        AspirationWindowsTest.class,

        AlphaBetaStatisticsTest.class,

        SearchTest.class,
        SymmetryTest.class,
        RepetitionTest.class,

        ChainPrinterVisitorTest.class,

        TTableArrayTest.class
})
public class SmartTestSuite {
}
