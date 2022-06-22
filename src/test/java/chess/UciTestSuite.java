/**
 * 
 */
package chess;

import chess.uci.engine.imp.EngineProxyTest;
import chess.uci.engine.imp.EngineZondaTest;
import chess.uci.engine.EngineMainTest;
import chess.uci.protocol.UCIDecoderCmdTest;
import chess.uci.protocol.UCIDecoderRspTest;
import chess.uci.ui.imp.EngineControllerImpTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ EngineMainTest.class, UCIDecoderCmdTest.class, UCIDecoderRspTest.class, EngineZondaTest.class,
        EngineProxyTest.class, EngineControllerImpTest.class, EngineMainTest.class})
public class UciTestSuite {

}
