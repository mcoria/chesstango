
package net.chesstango.uci;

import net.chesstango.uci.engine.UciTangoTest;
import net.chesstango.uci.gui.EngineControllerImpTangoTest;
import net.chesstango.uci.proxy.ProxyConfigTest;
import net.chesstango.uci.service.UciMainTangoTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({UciMainTangoTest.class, UciTangoTest.class,
        EngineControllerImpTangoTest.class, ProxyConfigTest.class})
public class UCIEngineTestSuite {

}
