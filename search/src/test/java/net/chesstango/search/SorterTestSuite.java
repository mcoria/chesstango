package net.chesstango.search;

import net.chesstango.search.smart.IterativeDeepeningTest;
import net.chesstango.search.smart.MoveSelectorTest;
import net.chesstango.search.smart.sorters.DefaultMoveSorterTest;
import net.chesstango.search.smart.sorters.MoveComparatorTest;
import net.chesstango.search.smart.sorters.TranspositionEntryMoveSorterTest;
import net.chesstango.search.smart.transposition.TranspositionEntryTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        DefaultMoveSorterTest.class,
        TranspositionEntryMoveSorterTest.class,
        MoveComparatorTest.class
})
public class SorterTestSuite {

}
