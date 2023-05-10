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
        MinMaxPrunningMateIn1Test.class,
        MinMaxPrunningMateIn2Test.class,
        MinMaxPrunningMateIn3Test.class,
        MinMaxPrunningMateIn4Test.class,
        Prunning01Test.class,
        WhiteBestMovesTest.class
})
public class AlphaBetaTestSuite {

}
