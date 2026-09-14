package net.chesstango.search;

import net.chesstango.search.alphabeta.evalcache.EvaluatorCacheDebugTest;
import net.chesstango.search.alphabeta.evalcache.EvaluatorCacheArrayTest;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationComparatorTest;
import net.chesstango.search.alphabeta.transposition.TTableComparatorHeadDebugTest;
import net.chesstango.search.alphabeta.transposition.TTableComparatorTailDebugTest;
import net.chesstango.search.alphabeta.transposition.TTableStatisticsCollectorTest;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionHeadMoveComparatorTest;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionTailMoveComparatorTest;
import net.chesstango.search.sorters.*;
import net.chesstango.search.sorters.comparators.DefaultMoveComparatorTest;
import net.chesstango.search.sorters.groupsorters.CatchAllSortGroupTest;
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
        TranspositionHeadMoveComparatorTest.class,
        TranspositionTailMoveComparatorTest.class,
        RootMoveEvaluationComparatorTest.class,

        /**
         * Sorters
         */
        NodeMoveSorterTest.class,
        NodeSorter01Test.class,
        NodeSorter02Test.class,
        NodeSorter03Test.class,

        CatchAllSortGroupTest.class,

        NodeGroupSorterTest.class,

        NodeMoveSorterInteriorTest.class,

        /**
         * Collectors
         */
        TTableStatisticsCollectorTest.class
})
public class SorterTestSuite {

}
