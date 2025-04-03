package net.chesstango.board.analyzer;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.builders.GameBuilderDebug;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class KingSafePositionsAnalyzerTest {

    private AnalyzerResult analyzerResult;

    @Test
    public void testKingInCheck01() {
        Game game = getGame("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");

        game.getStatus();

        // El king se encuentra en Jaque, e8 no es una posicion safe
        assertTrue((analyzerResult.getSafeKingPositions() & Square.e8.getBitPosition()) == 0);
    }

    @Test
    public void testKnights() {
        Game game = getGame("4k3/8/8/8/8/2n2n1n/8/4K3 w - - 0 1");

        game.getStatus();

        // El king se encuentra en Jaque, e1 no es una posicion safe
        assertTrue((analyzerResult.getSafeKingPositions() & Square.d1.getBitPosition()) == 0);
        assertTrue((analyzerResult.getSafeKingPositions() & Square.e1.getBitPosition()) == 0);
        assertTrue((analyzerResult.getSafeKingPositions() & Square.f1.getBitPosition()) != 0);

        assertTrue((analyzerResult.getSafeKingPositions() & Square.d2.getBitPosition()) == 0);
        assertTrue((analyzerResult.getSafeKingPositions() & Square.e2.getBitPosition()) == 0);
        assertTrue((analyzerResult.getSafeKingPositions() & Square.f2.getBitPosition()) == 0);

    }

    @Test
    public void testBishops() {
        Game game = getGame("B3k3/q7/b7/B7/8/8/4K3/8 w - - 0 1");

        game.getStatus();

        assertTrue((analyzerResult.getSafeKingPositions() & Square.d1.getBitPosition()) != 0);
        assertTrue((analyzerResult.getSafeKingPositions() & Square.e1.getBitPosition()) != 0);
        assertTrue((analyzerResult.getSafeKingPositions() & Square.f1.getBitPosition()) == 0);


        assertTrue((analyzerResult.getSafeKingPositions() & Square.d2.getBitPosition()) != 0);
        assertTrue((analyzerResult.getSafeKingPositions() & Square.e2.getBitPosition()) == 0);
        assertTrue((analyzerResult.getSafeKingPositions() & Square.f2.getBitPosition()) == 0);

        assertTrue((analyzerResult.getSafeKingPositions() & Square.d3.getBitPosition()) == 0);
        assertTrue((analyzerResult.getSafeKingPositions() & Square.e3.getBitPosition()) == 0);
        assertTrue((analyzerResult.getSafeKingPositions() & Square.f3.getBitPosition()) != 0);
    }

    @Test
    public void testPawns() {
        Game game = getGame("4k3/8/8/8/8/2pp4/5pp1/4K3 w - - 0 1");

        game.getStatus();

        assertEquals(0, (analyzerResult.getSafeKingPositions() & Square.e1.getBitPosition()));
        assertTrue((analyzerResult.getSafeKingPositions() & Square.d1.getBitPosition()) != 0);
        assertEquals(0, (analyzerResult.getSafeKingPositions() & Square.f1.getBitPosition()));

        assertEquals(0, (analyzerResult.getSafeKingPositions() & Square.d2.getBitPosition()));
        assertEquals(0, (analyzerResult.getSafeKingPositions() & Square.e2.getBitPosition()));
        assertTrue((analyzerResult.getSafeKingPositions() & Square.f2.getBitPosition()) != 0);
    }

    private Game getGame(String string) {
        GameBuilder builder = new GameBuilderDebug(new ChessFactoryDebug() {

            public KingSafePositionsAnalyzer createKingSafePositionsAnalyzer(ChessPositionReader positionReader) {
                return new KingSafePositionsAnalyzer(positionReader){
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
