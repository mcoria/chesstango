/**
 * 
 */
package chess;

import chess.uci.engine.EngineProxyTest;
import chess.uci.engine.EngineZondaTest;
import chess.uci.engine.MainTest;
import chess.uci.protocol.UCIDecoderCmdTest;
import chess.uci.protocol.UCIDecoderRspTest;
import chess.uci.ui.EngineClientImpTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MainTest.class, UCIDecoderCmdTest.class, UCIDecoderRspTest.class, EngineZondaTest.class,
        EngineProxyTest.class, EngineClientImpTest.class, chess.uci.ui.MainTest.class})
public class UciTestSuite {

}
