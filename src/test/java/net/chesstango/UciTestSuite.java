/**
 * 
 */
package net.chesstango;

import net.chesstango.uci.arbiter.MatchTest;
import net.chesstango.uci.engine.imp.EngineProxyTest;
import net.chesstango.uci.engine.imp.EngineTangoTest;
import net.chesstango.uci.engine.EngineMainTest;
import net.chesstango.uci.protocol.UCIDecoderCmdTest;
import net.chesstango.uci.protocol.UCIDecoderRspTest;
import net.chesstango.uci.arbiter.imp.EngineControllerImpTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ EngineMainTest.class, UCIDecoderCmdTest.class, UCIDecoderRspTest.class, EngineTangoTest.class,
        EngineProxyTest.class, EngineControllerImpTest.class, EngineMainTest.class, MatchTest.class})
public class UciTestSuite {

}
