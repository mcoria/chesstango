package net.chesstango.board.analyzer;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.builders.GameBuilderDebug;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class PositionAnalyzerTest {

    private AnalyzerResult analyzerResult;

    @Test
    public void testKingInCheck01() {
        Game game = getGame("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");

        analyzerResult = game.getState().getAnalyzerResult();

        Assertions.assertTrue(analyzerResult.isKingInCheck());
    }

    @Test
    public void testPinnedPositions01() {
        Game game = getGame("rnb1kbnr/pp1ppppp/8/q1p5/1P6/3P4/P1PKPPPP/RNBQ1BNR b kq - 0 4");

        game.executeMove(Square.d7, Square.d6);

        analyzerResult = game.getState().getAnalyzerResult();

        long pinnedSquares = analyzerResult.getPinnedSquares();
        Assertions.assertNotEquals(0, pinnedSquares);
        Assertions.assertTrue((pinnedSquares & Square.b4.getBitPosition()) != 0);
        Assertions.assertFalse(analyzerResult.isKingInCheck());
    }

    @Test
    public void testPinnedPositions02() {
        Game game = getGame("r3k2r/p1pp1pb1/bn1qpnpB/3PN3/1p2P3/2N2Q1p/PPP1BPPP/R2K3R b kq - 3 2");

        game.executeMove(Square.c7, Square.c5);

        analyzerResult = game.getState().getAnalyzerResult();

        long pinnedSquares = analyzerResult.getPinnedSquares();
        Assertions.assertTrue((pinnedSquares & Square.d5.getBitPosition()) != 0);
        Assertions.assertFalse(analyzerResult.isKingInCheck());
    }

    private Game getGame(String string) {
        GameBuilder builder = new GameBuilderDebug();

        FENDecoder parser = new FENDecoder(builder);

        parser.parseFEN(string);

        return builder.getChessRepresentation();
    }
}
