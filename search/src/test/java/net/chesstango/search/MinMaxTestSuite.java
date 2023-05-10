package net.chesstango.search;

import net.chesstango.search.smart.minmax.MinMaxMateIn1Test;
import net.chesstango.search.smart.minmax.MinMaxMateIn2Test;
import net.chesstango.search.smart.minmax.MinMaxTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        MinMaxMateIn1Test.class,
        MinMaxMateIn2Test.class,
        MinMaxTest.class,
})
public class MinMaxTestSuite {

}
