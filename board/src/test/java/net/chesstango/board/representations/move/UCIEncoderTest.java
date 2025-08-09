package net.chesstango.board.representations.move;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 *
 */
public class UCIEncoderTest {
    private SimpleMoveEncoder encoder;

    private MoveFactory moveFactoryWhite;

    @BeforeEach
    public void setup(){
        encoder = new SimpleMoveEncoder();
        moveFactoryWhite = new MoveFactoryWhite();
    }

    @Test
    public void testEncodePromotion01(){
        Game game = Game.from(FEN.of("r1bqkbnr/1p5p/p4pp1/3p4/3Pp2Q/8/PpPP1PPP/R1B1R1K1 b kq - 1 15"));

        String moveStr = "b2a1q";
        Move theMove = null;
        for (Move move : game.getPossibleMoves()) {
            String encodedMoveStr = encoder.encode(move);
            if (encodedMoveStr.equals(moveStr)) {
                theMove = move;
                break;
            }
        }
        assertNotNull(theMove);

        assertTrue(theMove instanceof MovePromotion);

        MovePromotion movePromotion = (MovePromotion) theMove;

        assertEquals(Piece.QUEEN_BLACK, movePromotion.getPromotion());
    }

    @Test
    public void testEncodePromotion02(){
        Move promotionToKnight = moveFactoryWhite.createSimplePromotionPawnMove(PiecePositioned.of(Square.g7, Piece.PAWN_WHITE), PiecePositioned.getPosition(Square.g8), Piece.KNIGHT_WHITE);

        String encodedMoveStr = encoder.encode(promotionToKnight);

        assertEquals("g7g8n", encodedMoveStr);
    }

}
