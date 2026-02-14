package net.chesstango.search;

import net.chesstango.search.smart.sorters.DefaultMoveSorterTest;
import net.chesstango.search.smart.sorters.NodeSorter01Test;
import net.chesstango.search.smart.sorters.NodeSorter02Test;
import net.chesstango.search.smart.sorters.NodeSorter03Test;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparatorReversedTest;
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
        DefaultMoveComparatorReversedTest.class,
        TranspositionHeadMoveComparatorTest.class,

        DefaultMoveSorterTest.class,
        NodeSorter01Test.class,
        NodeSorter02Test.class,
        NodeSorter03Test.class
})
public class SorterTestSuite {

}
