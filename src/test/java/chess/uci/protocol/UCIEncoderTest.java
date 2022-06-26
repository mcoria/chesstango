package chess.uci.protocol;

import chess.board.Game;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;
import chess.board.moves.MoveFactory;
import chess.board.moves.MovePromotion;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.representations.fen.FENDecoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class UCIEncoderTest {
    private UCIEncoder encoder;

    private MoveFactory moveFactoryWhite;

    @Before
    public void setup(){
        encoder = new UCIEncoder();
        moveFactoryWhite = new MoveFactoryWhite();
    }

    @Test
    public void testEncodePromotion01(){
        Game game = FENDecoder.loadGame("r1bqkbnr/1p5p/p4pp1/3p4/3Pp2Q/8/PpPP1PPP/R1B1R1K1 b kq - 1 15");

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
        Move promotionToKnight = moveFactoryWhite.createSimplePawnPromotion(PiecePositioned.getPiecePositioned(Square.g7, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.g8), Piece.KNIGHT_WHITE);

        String encodedMoveStr = encoder.encode(promotionToKnight);

        Assert.assertEquals("g7g8n", encodedMoveStr);
    }

}
