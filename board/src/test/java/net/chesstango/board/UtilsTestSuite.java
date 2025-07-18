package net.chesstango.board;


import net.chesstango.board.representations.pgn.PGNEncoderTest;
import net.chesstango.board.representations.pgn.PGNGameTest;
import net.chesstango.board.representations.pgn.PGNStringDecoderTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        PGNEncoderTest.class,
        PGNGameTest.class,
        PGNStringDecoderTest.class})
public class UtilsTestSuite {

}
