/**
 * 
 */
package chess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.board.representations.ascii.ASCIIEncoderTest;
import chess.board.representations.fen.FENDecoderTest;
import chess.board.representations.fen.FENEncoderTest;
import chess.board.representations.fen.FENIntegrationTest;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ASCIIEncoderTest.class, FENDecoderTest.class, FENEncoderTest.class, FENIntegrationTest.class})
public class UtilsTestSuite {

}
