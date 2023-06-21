package net.chesstango.board.analyzer;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.MoveCacheBoard;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class CheckAndPinnedAnalyzerTest {

    private AnalyzerResult analyzerResult;

    @Test
    public void testCheckKingInCheck() {
        Game game = getGame("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");

        game.getStatus();

        Assertions.assertTrue(analyzerResult.isKingInCheck());
    }


    @Test
    public void testPinnedPositions() {
        Game game = getGame("rnb1kbnr/pp1ppppp/8/q1p5/1P6/3P4/P1PKPPPP/RNBQ1BNR b kq - 0 4");

        game.executeMove(Square.d7, Square.d6);
        game.getStatus();

        long pinnedSquares = analyzerResult.getPinnedSquares();
        Assertions.assertNotEquals(0, pinnedSquares);
        Assertions.assertTrue((pinnedSquares & Square.d4.getBitPosition()) != 0);
        Assertions.assertTrue(analyzerResult.isKingInCheck());
    }


    private Game getGame(String string) {
        GameBuilder builder = new GameBuilder(new ChessFactoryDebug() {
            @Override
            public CheckAndPinnedAnalyzer createCheckAndPinnedAnalyzer(ChessPositionReader positionReader, MoveCacheBoard moveCacheBoard) {
                return new CheckAndPinnedAnalyzer(positionReader, moveCacheBoard) {
                    @Override
                    public void analyze(AnalyzerResult result) {
                        super.analyze(result);
                        CheckAndPinnedAnalyzerTest.this.analyzerResult = result;
                    }
                };
            }
        });

        FENDecoder parser = new FENDecoder(builder);

        parser.parseFEN(string);

        return builder.getChessRepresentation();
    }
}
