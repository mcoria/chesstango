/**
 *
 */
package net.chesstango.evaluation;

import net.chesstango.evaluation.imp.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({
        GameEvaluatorByMaterialAndMovesTest.class,
        GameEvaluatorImp01Test.class,
        GameEvaluatorImp02Test.class,
        GameEvaluatorImp03Test.class,
        GameEvaluatorSimplifiedEvaluatorTest.class
})
public class EvaluatorTestSuite {

}
