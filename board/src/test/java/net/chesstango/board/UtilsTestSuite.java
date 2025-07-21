package net.chesstango.board;


import net.chesstango.board.representations.pgn.GameToPGNTest;
import net.chesstango.board.representations.pgn.PGNToGameTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 */
@Suite
@SelectClasses({
        GameToPGNTest.class,
        PGNToGameTest.class,
        GameToPGNTest.class})
public class UtilsTestSuite {

}
