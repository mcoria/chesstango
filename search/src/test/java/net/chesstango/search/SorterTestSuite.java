package net.chesstango.search;

import net.chesstango.search.smart.sorters.*;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparatorTest;
import net.chesstango.search.smart.sorters.comparators.TranspositionHeadMoveComparatorTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        DefaultMoveComparatorTest.class,
        TranspositionHeadMoveComparatorTest.class,

        NodeMoveSorterTest.class,
        NodeSorter01Test.class,
        NodeSorter02Test.class,
        NodeSorter03Test.class,

        RootMoveEvaluationComparatorTest.class
})
public class SorterTestSuite {

}
