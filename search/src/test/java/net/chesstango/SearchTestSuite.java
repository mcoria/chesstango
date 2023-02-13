/**
 * 
 */
package net.chesstango;

import net.chesstango.ai.BestMoveFinderSuiteTest;
import net.chesstango.ai.imp.smart.*;
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
        BlackBestMovesTest.class,
        IterativeDeepingTest.class,
        BestMoveFinderSuiteTest.class,
        MateIn1Test.class,
        MateIn2Test.class,
        MateIn3Test.class,
        MateIn4Test.class
})
public class SearchTestSuite {

}
