/**
 * 
 */
package chess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.board.ascii.ASCIIEncoderTest;
import chess.board.fen.FENDecoderTest;
import chess.board.fen.FENEncoderTest;
import chess.board.fen.FENIntegrationTest;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ASCIIEncoderTest.class, FENDecoderTest.class, FENEncoderTest.class, FENIntegrationTest.class})
public class UtilsTestSuite {

}
