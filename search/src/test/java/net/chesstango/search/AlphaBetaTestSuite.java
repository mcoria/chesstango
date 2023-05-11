package net.chesstango.search;


import net.chesstango.search.smart.alphabeta.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        AlphaBetaStatisticsTest.class,
        BlackBestMovesTest.class,
        DetectCycleTest.class,
        MinMaxPruningBlackTest.class,
        MinMaxPruningWhiteTest.class,
        MinMaxPruningMateIn1Test.class,
        MinMaxPruningMateIn2Test.class,
        MinMaxPruningMateIn3Test.class,
        MinMaxPruningMateIn4Test.class,
        Pruning01Test.class,
        WhiteBestMovesTest.class
})
public class AlphaBetaTestSuite {

}
