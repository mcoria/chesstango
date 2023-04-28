package net.chesstango.search;


import net.chesstango.search.smart.negamax.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({
        BlackBestMovesTest.class,
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
        WhiteBestMovesTest.class
})
public class NegaMaxTestSuite {

}
