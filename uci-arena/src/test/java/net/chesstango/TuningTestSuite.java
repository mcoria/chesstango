/**
 * 
 */
package net.chesstango;


import net.chesstango.uci.arbiter.MatchTest;
import net.chesstango.uci.arbiter.imp.EngineControllerImpTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ EngineControllerImpTest.class, MatchTest.class })
public class TuningTestSuite {

}
