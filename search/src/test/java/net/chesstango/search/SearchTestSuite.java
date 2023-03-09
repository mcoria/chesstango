/**
 *
 */
package net.chesstango.search;

import net.chesstango.search.smart.*;
import net.chesstango.search.smart.minmax.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        MinMaxTest.class,
        MinMaxPruningWhiteTest.class,
        MinMaxPruningBlackTest.class,
        MinMaxPruning01Test.class,
        MoveSorterTest.class,
        AbstractBlackBestMovesTest.class,
        IterativeDeepingTest.class,
        SearchMoveSuiteTest.class,
        MateIn1Test.class,
        MateIn2Test.class,
        MateIn3Test.class,
        MateIn4Test.class
})
public class SearchTestSuite {

}
