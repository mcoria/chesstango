package net.chesstango.search;

import net.chesstango.search.smart.minmax.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({
        MinMaxMateIn1Test.class,
        MinMaxMateIn2Test.class,
        MinMaxTest.class,
})
public class MinMaxTestSuite {

}
