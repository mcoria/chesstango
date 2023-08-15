
package net.chesstango.uci;

import net.chesstango.uci.engine.EngineTangoTest;
import net.chesstango.uci.gui.EngineControllerImpTangoTest;
import net.chesstango.uci.proxy.ProxyConfigTest;
import net.chesstango.uci.service.ServiceMainTangoTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ServiceMainTangoTest.class, EngineTangoTest.class,
        EngineControllerImpTangoTest.class, ProxyConfigTest.class})
public class UCIEngineTestSuite {

}
