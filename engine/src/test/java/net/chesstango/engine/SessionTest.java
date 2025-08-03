package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class SessionTest {

    @Test
    public void testSetPosition() {
        Session session = new Session(FEN.of(FENParser.INITIAL_FEN), null);

        String movesStr = "e2e4 c7c5 g1f3 d7d6 d2d3 b8c6 g2g3 g7g6 f1g2 f8g7 e1g1 g8f6 c2c3 e7e5 b1d2 e8g8 f1e1 d8c7 d2f1 d6d5 e4d5 f6d5 f1e3 d5e3 c1e3 c6e7 a1c1 e7f5 e3d2 c8e6 b2b3 e6d5 c3c4 d5c6 d2c3 f8e8 d1c2 a8d8 c1d1 h7h6 c2b2 e5e4 d3e4 d8d1 e1d1 c6e4 c3g7 f5g7 d1e1 c7d6 f3d2 e4g2 e1e8 g7e8 g1g2 e8f6 d2f3 g6g5 b2e5 d6e5 f3e5 g5g4 f2f3 g4f3 g2f3 g8g7 f3f4 f6h7 e5d7 b7b6 h2h4 h7f8 d7e5 f7f6 e5c6 a7a5 f4f5 g7f7 c6d8 f7e7 d8c6 e7f7 c6d8 f7e7 d8c6";

        List<String> moves = Arrays.stream(movesStr.split(" ")).toList();

        session.setMoves(moves);

        Game game = session.getGame();

        assertTrue(game.getStatus().isInProgress());
    }

}
