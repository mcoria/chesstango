package net.chesstango.search;

import net.chesstango.search.smart.AbstractSmartTest;
import net.chesstango.search.smart.minmax.BlackBestMovesTest;
import net.chesstango.search.smart.MoveSorterTest;
import net.chesstango.search.smart.minmax.Prunning01Test;
import net.chesstango.search.smart.minmax.WhiteBestMovesTest;
import net.chesstango.search.smart.minmax.*;
import net.chesstango.search.smart.negamax.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BlackBestMovesTest.class,
        MinMaxMateIn1Test.class,
        MinMaxMateIn2Test.class,
        MinMaxPruningBlackTest.class,
        MinMaxPruningWhiteTest.class,
        MinMaxPrunningMateIn1Test.class,
        MinMaxPrunningMateIn2Test.class,
        MinMaxPrunningMateIn3Test.class,
        MinMaxPrunningMateIn4Test.class,
        MinMaxTest.class,
        MoveSorterTest.class,
        Prunning01Test.class,
        WhiteBestMovesTest.class,

        BlackBestMovesTest.class,
        MoveSorterTest.class,
        NagaMaxPruningWhiteTest.class,
        NegaMaxMateIn1Test.class,
        NegaMaxMateIn2Test.class,
        NegaMaxPruningBlackTest.class,
        NegaMaxPrunningMateIn1Test.class,
        NegaMaxPrunningMateIn2Test.class,
        NegaMaxPrunningMateIn3Test.class,
        NegaMaxPrunningMateIn4Test.class,
        NegaMaxTest.class,
        Prunning01Test.class,
        WhiteBestMovesTest.class,

        AbstractSmartTest.class,

        SearchMoveSuiteTest.class
})
public class SearchTestSuite {

}
