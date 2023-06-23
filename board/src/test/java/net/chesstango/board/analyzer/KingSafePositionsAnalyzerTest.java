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
public class KingSafePositionsAnalyzerTest {

    private AnalyzerResult analyzerResult;

    //@Test
    public void testKingInCheck01() {
        Game game = getGame("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");

        game.getStatus();

        // El king se encuentra en Jaque, e8 no es una posicion safe
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.e8.getBitPosition()) == 0);
    }

    @Test
    public void testKnights() {
        Game game = getGame("4k3/8/8/8/8/2n2n1n/8/4K3 w - - 0 1");

        game.getStatus();

        // El king se encuentra en Jaque, e1 no es una posicion safe
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.e1.getBitPosition()) == 0);

        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.d1.getBitPosition()) == 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.d2.getBitPosition()) == 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.e2.getBitPosition()) == 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.f2.getBitPosition()) == 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.f1.getBitPosition()) != 0);
    }

    @Test
    public void testKings() {
        Game game = getGame("8/8/8/8/8/4k3/8/4K3 w - - 0 1");

        game.getStatus();

        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.e1.getBitPosition()) != 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.d1.getBitPosition()) != 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.f1.getBitPosition()) != 0);

        // No puede subir el rey
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.d2.getBitPosition()) == 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.e2.getBitPosition()) == 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.f2.getBitPosition()) == 0);
    }

    @Test
    public void testPawns() {
        Game game = getGame("4k3/8/8/8/8/2pp4/5pp1/4K3 w - - 0 1");

        game.getStatus();

        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.e1.getBitPosition()) == 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.d1.getBitPosition()) != 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.f1.getBitPosition()) == 0);

        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.d2.getBitPosition()) == 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.e2.getBitPosition()) == 0);
        Assertions.assertTrue((analyzerResult.getSafeKingPositions() & Square.f2.getBitPosition()) != 0);
    }

    private Game getGame(String string) {
        GameBuilder builder = new GameBuilder(new ChessFactoryDebug() {
            @Override
            public CheckAnalyzer createCheckAnalyzer(ChessPositionReader positionReader, MoveCacheBoard moveCacheBoard) {
                return new CheckAnalyzer(positionReader, moveCacheBoard) {
                    @Override
                    public void analyze(AnalyzerResult result) {
                        super.analyze(result);
                        analyzerResult = result;
                    }
                };
            }
        });

        FENDecoder parser = new FENDecoder(builder);

        parser.parseFEN(string);

        return builder.getChessRepresentation();
    }

}
