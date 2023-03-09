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
        SearchMoveSuiteTest.class,
})
public class SearchTestSuite {

}
