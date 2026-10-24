package net.chesstango.search;

import net.chesstango.search.alphabeta.evalcache.EvaluatorCacheArrayTest;
import net.chesstango.search.alphabeta.evalcache.EvaluatorCacheDebugTest;
import net.chesstango.search.alphabeta.pv.groupsorters.PrincipalVariationGroupTest;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationComparatorTest;
import net.chesstango.search.alphabeta.transposition.TTableComparatorHeadDebugTest;
import net.chesstango.search.alphabeta.transposition.TTableComparatorTailDebugTest;
import net.chesstango.search.alphabeta.transposition.TTableStatisticsCollectorTest;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionTailMoveComparatorTest;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionHeadMoveGroupTest;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionTailMoveGroupTest;
import net.chesstango.search.sorters.*;
import net.chesstango.search.sorters.comparators.DefaultMoveComparatorTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        /**
         * Debug
         */
        MoveSorterDebugTest.class,
        TTableComparatorHeadDebugTest.class,
        TTableComparatorTailDebugTest.class,
        EvaluatorCacheDebugTest.class,
        EvaluatorCacheArrayTest.class,

        /**
         * Comparators
         */
        DefaultMoveComparatorTest.class,
        TranspositionTailMoveComparatorTest.class,
        RootMoveEvaluationComparatorTest.class,

        /**
         * Sorters
         */
        NodeMoveSorterTest.class,
        NodeSorter01Test.class,
        NodeSorter02Test.class,
        NodeSorter03Test.class,

        NodeMoveSorterInteriorTest.class,


        /**
         * Group Sorters
         */
        NodeMoveSorterStageCatAllTest.class,
        NodeMoveSorterStagePVTest.class,
        NodeMoveSorterStageTranspositionHeadTest.class,
        NodeMoveSorterStageTranspositionTailTest.class,

        /**
         * Move Groups
         */
        TranspositionHeadMoveGroupTest.class,
        PrincipalVariationGroupTest.class,
        TranspositionTailMoveGroupTest.class,


        /**
         * Collectors
         */
        TTableStatisticsCollectorTest.class
})
public class SorterTestSuite {

}
