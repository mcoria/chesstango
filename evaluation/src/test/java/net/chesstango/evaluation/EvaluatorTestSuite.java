
package net.chesstango.evaluation;

import net.chesstango.evaluation.evaluators.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({
        EvaluatorByMaterialAndMovesTest.class,
        EvaluatorImp01Test.class,
        EvaluatorImp02Test.class,
        EvaluatorImp03Test.class,
        EvaluatorTestCollection.class,
})
public class EvaluatorTestSuite {

}
