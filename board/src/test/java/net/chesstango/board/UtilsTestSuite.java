package net.chesstango.board;

import net.chesstango.board.representations.AbstractPositionBuilderTest;
import net.chesstango.board.representations.move.SANDecoderTest;
import net.chesstango.board.representations.move.SANEncoderTest;
import net.chesstango.board.representations.pgn.PGNStringDecoderTest;
import net.chesstango.board.representations.ascii.ASCIIBuilderTest;
import net.chesstango.board.representations.fen.FENExporterTest;
import net.chesstango.board.representations.fen.FENBuilderTest;
import net.chesstango.board.representations.fen.FENIntegrationTest;
import net.chesstango.board.representations.pgn.PGNDecoderTest;
import net.chesstango.board.representations.pgn.PGNEncoderTest;
import net.chesstango.board.representations.pgn.PGNGameTest;
import net.chesstango.board.representations.polyglot.PolyglotKeyBuilderTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ AbstractPositionBuilderTest.class, ASCIIBuilderTest.class, FENExporterTest.class, FENBuilderTest.class, FENIntegrationTest.class,
        SANEncoderTest.class, SANDecoderTest.class, PGNEncoderTest.class, PGNDecoderTest.class, PGNGameTest.class,
        PGNStringDecoderTest.class, PolyglotKeyBuilderTest.class})
public class UtilsTestSuite {

}
