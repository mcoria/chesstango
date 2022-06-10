/**
 * 
 */
package chess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.uci.engine.MainTest;
import chess.uci.engine.EngineZondaTest;
import chess.uci.protocol.UCIDecoderTest;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MainTest.class, UCIDecoderTest.class, EngineZondaTest.class})
public class UciTestSuite {

}
