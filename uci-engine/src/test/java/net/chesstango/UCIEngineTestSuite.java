/**
 * 
 */
package net.chesstango;

import net.chesstango.uci.engine.proxy.UCIServiceProxyTest;
import net.chesstango.uci.engine.UCIServiceTangoTest;
import net.chesstango.uci.engine.UCIServiceMainTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ UCIServiceMainTest.class, UCIServiceTangoTest.class, UCIServiceProxyTest.class, UCIServiceMainTest.class})
public class UCIEngineTestSuite {

}
