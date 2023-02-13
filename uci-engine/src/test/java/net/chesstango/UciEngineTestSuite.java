/**
 * 
 */
package net.chesstango;

import net.chesstango.uci.engine.imp.EngineProxyTest;
import net.chesstango.uci.engine.imp.EngineTangoTest;
import net.chesstango.uci.engine.EngineMainTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ EngineMainTest.class, EngineTangoTest.class, EngineProxyTest.class, EngineMainTest.class})
public class UciEngineTestSuite {

}
