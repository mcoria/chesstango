/**
 * 
 */
package net.chesstango.evaluation;

import net.chesstango.evaluation.GameEvaluatorTest;
import net.chesstango.evaluation.imp.GameEvaluatorImp01Test;
import net.chesstango.evaluation.imp.GameEvaluatorImp02Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GameEvaluatorTest.class,
        GameEvaluatorImp01Test.class,
        GameEvaluatorImp02Test.class
})
public class EvaluatorTestSuite {

}
