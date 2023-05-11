package net.chesstango.search;

import net.chesstango.search.smart.BinaryUtilsTest;
import net.chesstango.search.smart.MoveSelectorTest;
import net.chesstango.search.smart.sorters.DefaultMoveSorterTest;
import net.chesstango.search.smart.sorters.MoveComparatorTest;
import net.chesstango.search.smart.sorters.TranspositionMoveSorterTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        DefaultMoveSorterTest.class,
        TranspositionMoveSorterTest.class,
        MoveSelectorTest.class,
        MoveComparatorTest.class,
        BinaryUtilsTest.class
})
public class SmartTestSuite {

}
