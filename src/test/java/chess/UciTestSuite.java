/**
 * 
 */
package chess;

import chess.uci.protocol.UCIDecoder02Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.uci.engine.MainTest;
import chess.uci.engine.EngineZondaTest;
import chess.uci.protocol.UCIDecoder01Test;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MainTest.class, UCIDecoder01Test.class, UCIDecoder02Test.class, EngineZondaTest.class})
public class UciTestSuite {

}
