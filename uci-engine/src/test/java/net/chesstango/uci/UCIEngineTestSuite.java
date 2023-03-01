/**
 *
 */
package net.chesstango.uci;

import net.chesstango.uci.engine.EngineTangoTest;
import net.chesstango.uci.gui.EngineControllerImpTest;
import net.chesstango.uci.proxy.EngineProxyTest;
import net.chesstango.uci.service.ServiceMainTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ServiceMainTest.class, EngineTangoTest.class, EngineProxyTest.class, EngineControllerImpTest.class})
public class UCIEngineTestSuite {

}
