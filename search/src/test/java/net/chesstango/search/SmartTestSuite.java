package net.chesstango.search;

import net.chesstango.search.smart.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({
        MoveSorterTest.class,
        MoveSorterTest.class,
        MoveSelectorTest.class
})
public class SmartTestSuite {

}
