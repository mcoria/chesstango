package net.chesstango.board.representations.move;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 *
 */
public class LANDecoderTest {

    private LANDecoder decoder = new LANDecoder();

    @Test
    public void testDecodeLAN01_Promotion() {
        Game game = Game.fromFEN("8/kpP2r1p/p6r/n3Q1p1/P6q/1PN3p1/5P2/2RR2K1 w - - 0 1");

        Move expectedMove = game.getMove(Square.c7, Square.c8, Piece.KNIGHT_WHITE);
        Move decodedMove = decoder.decode("c7-c8N+", game.getPossibleMoves());

        assertEquals(expectedMove, decodedMove);
    }
}
