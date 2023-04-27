/**
 *
 */
package net.chesstango.uci;

import net.chesstango.uci.engine.EngineTangoTest;
import net.chesstango.uci.gui.EngineControllerImpZondaTest;
import net.chesstango.uci.proxy.EngineProxyTest;
import net.chesstango.uci.proxy.ProxyConfigTest;
import net.chesstango.uci.service.ServiceMainTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ServiceMainTest.class, EngineTangoTest.class, EngineProxyTest.class,
        EngineControllerImpZondaTest.class, ProxyConfigTest.class})
public class UCIEngineTestSuite {

}
