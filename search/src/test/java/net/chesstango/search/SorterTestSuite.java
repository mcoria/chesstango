package net.chesstango.search;

import net.chesstango.search.smart.evaluator.EvaluatorCacheDebugTest;
import net.chesstango.search.smart.root.RootMoveEvaluationComparatorTest;
import net.chesstango.search.smart.transposition.TTableComparatorHeadDebugTest;
import net.chesstango.search.smart.transposition.TTableComparatorTailDebugTest;
import net.chesstango.search.smart.transposition.comparators.TranspositionHeadMoveComparatorTest;
import net.chesstango.search.smart.transposition.comparators.TranspositionTailMoveComparatorTest;
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

        NodeMoveSorterInteriorTest.class
})
public class SorterTestSuite {

}
