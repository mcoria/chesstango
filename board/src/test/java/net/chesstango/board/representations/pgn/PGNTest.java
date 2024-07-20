package net.chesstango.board.representations.pgn;

import net.chesstango.board.Game;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static net.chesstango.board.Square.a2;
import static net.chesstango.board.Square.a4;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class PGNTest {

    @Test
    public void testToEpd() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        game.executeMove(a2, a4);

        PGN pgn = PGN.of(game);

        List<EPD> pgnToEpd = pgn.toEPD().toList();

        assertEquals(1, pgnToEpd.size());
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - sm a4; c5 \"result='*'\"; c6 \"clock=1\"; id \"463b96181691fc9c\";", pgnToEpd.getFirst().toString());
    }
}
