/**
 *
 */
package net.chesstango.evaluation;

import net.chesstango.evaluation.imp.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GameEvaluatorByMaterialAndMovesTest.class,
        GameEvaluatorImp01Test.class,
        GameEvaluatorImp02Test.class,
        GameEvaluatorImp03Test.class,
        GameEvaluatorSimplifiedEvaluatorTest.class
})
public class EvaluatorTestSuite {

}
