/**
 * 
 */
package chess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.uci.MainTest;
import chess.uci.engine.EngineTest;
import chess.uci.protocol.UCIDecoderTest;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MainTest.class, UCIDecoderTest.class, EngineTest.class})
public class UciTestSuite {

}
