/**
 * 
 */
package net.chesstango;

import net.chesstango.ai.imp.smart.evaluation.GameEvaluatorTest;
import net.chesstango.ai.imp.smart.evaluation.imp.GameEvaluatorImp01Test;
import net.chesstango.ai.imp.smart.evaluation.imp.GameEvaluatorImp02Test;
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
