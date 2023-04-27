/**
 *
 */
package net.chesstango.uci;

import net.chesstango.uci.engine.EngineTangoTest;
import net.chesstango.uci.gui.EngineControllerImpZondaTest;
import net.chesstango.uci.proxy.EngineProxyTest;
import net.chesstango.uci.proxy.ProxyConfigTest;
import net.chesstango.uci.service.ServiceMainTest;

import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ServiceMainTest.class, EngineTangoTest.class, EngineProxyTest.class,
        EngineControllerImpZondaTest.class, ProxyConfigTest.class})
public class UCIEngineTestSuite {

}
