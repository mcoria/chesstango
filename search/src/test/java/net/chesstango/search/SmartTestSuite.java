package net.chesstango.search;

import net.chesstango.search.smart.IterativeDeepeningTest;
import net.chesstango.search.smart.MoveSelectorTest;
import net.chesstango.search.smart.transposition.TranspositionEntryTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        IterativeDeepeningTest.class,
        MoveSelectorTest.class,
        TranspositionEntryTest.class
})
public class SmartTestSuite {

}
