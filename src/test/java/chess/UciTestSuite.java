/**
 * 
 */
package chess;

import chess.uci.protocol.UCIDecoderRspTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.uci.engine.MainTest;
import chess.uci.engine.EngineZondaTest;
import chess.uci.protocol.UCIDecoderCmdTest;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MainTest.class, UCIDecoderCmdTest.class, UCIDecoderRspTest.class, EngineZondaTest.class})
public class UciTestSuite {

}
