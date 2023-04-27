/**
 * 
 */
package net.chesstango;

import net.chesstango.board.representations.SANDecoderTest;
import net.chesstango.board.representations.SANEncoderTest;
import net.chesstango.board.representations.TranscodingTest;
import net.chesstango.board.representations.ascii.ASCIIEncoderTest;
import net.chesstango.board.representations.fen.FENDecoderTest;
import net.chesstango.board.representations.fen.FENEncoderTest;
import net.chesstango.board.representations.fen.FENIntegrationTest;
import net.chesstango.board.representations.pgn.PGNDecoderTest;
import net.chesstango.board.representations.pgn.PGNEncoderTest;
import net.chesstango.board.representations.pgn.PGNGameTest;
import net.chesstango.board.representations.polyglot.PolyglotEncoderTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ ASCIIEncoderTest.class, FENDecoderTest.class, FENEncoderTest.class, FENIntegrationTest.class,
        SANEncoderTest.class, SANDecoderTest.class, PGNEncoderTest.class, PGNDecoderTest.class, PGNGameTest.class,
        TranscodingTest.class, PolyglotEncoderTest.class})
public class UtilsTestSuite {

}
