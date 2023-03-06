/**
 * 
 */
package net.chesstango;

import net.chesstango.board.representations.SANDecoderTest;
import net.chesstango.board.representations.pgn.PGNDecoderTest;
import net.chesstango.board.representations.pgn.PGNEncoderTest;
import net.chesstango.board.representations.SANEncoderTest;
import net.chesstango.board.representations.ascii.ASCIIEncoderTest;
import net.chesstango.board.representations.fen.FENDecoderTest;
import net.chesstango.board.representations.fen.FENEncoderTest;
import net.chesstango.board.representations.fen.FENIntegrationTest;
import net.chesstango.board.representations.pgn.PGNGameTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ASCIIEncoderTest.class, FENDecoderTest.class, FENEncoderTest.class, FENIntegrationTest.class,
        SANEncoderTest.class, SANDecoderTest.class, PGNEncoderTest.class, PGNDecoderTest.class, PGNGameTest.class})
public class UtilsTestSuite {

}
