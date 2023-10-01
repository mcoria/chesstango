package net.chesstango.search;


import net.chesstango.search.smart.negamax.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        BestMovesBlackTest.class,
        NegaMaxMateIn1Test.class,
        NegaMaxMateIn2Test.class,
        NegaMaxPruningTest.class,
        NegaMaxPruningMateIn1Test.class,
        NegaMaxPruningMateIn2Test.class,
        NegaMaxPruningMateIn3Test.class,
        NegaMaxPruningMateIn4Test.class,
        NegaMaxTest.class,
        NegaMaxPruningGenericTest.class,
        BestMovesWhiteTest.class
})
public class NegaMaxTestSuite {

}
