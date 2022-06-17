/**
 * 
 */
package chess;

import chess.board.representations.PGNEncoderTest;
import chess.board.representations.SANEncoderTest;
import chess.board.representations.ascii.ASCIIEncoderTest;
import chess.board.representations.fen.FENDecoderTest;
import chess.board.representations.fen.FENEncoderTest;
import chess.board.representations.fen.FENIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ASCIIEncoderTest.class, FENDecoderTest.class, FENEncoderTest.class, FENIntegrationTest.class,
        SANEncoderTest.class, PGNEncoderTest.class})
public class UtilsTestSuite {

}
