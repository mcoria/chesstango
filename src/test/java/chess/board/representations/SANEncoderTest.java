package chess.board.representations;

import chess.board.*;
import chess.board.moves.Move;
import chess.board.representations.fen.FENDecoder;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SANEncoderTest {

    private SANEncoder encoder = new SANEncoder();


    @Test
    public void test_pawnMove1(){
        Game game =  FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        Move move = game.getMove(Square.e2, Square.e3);

        String encodedMove = encoder.encode(move, game.getPossibleMoves());

        Assert.assertEquals("e3", encodedMove);
    }



    @Test
    public void test_pawnMove_capture(){
        Game game =  FENDecoder.loadGame("rnbqkbnr/ppp1p1pp/8/3p1p2/2P1P3/8/PP1P1PPP/RNBQKBNR w KQkq f6 0 3");

        Move move = null;
        String encodedMove = null;

        move = game.getMove(Square.e4, Square.f5);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("exf5", encodedMove);

        move = game.getMove(Square.c4, Square.d5);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("cxd5", encodedMove);

        move = game.getMove(Square.e4, Square.d5);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("exd5", encodedMove);
    }


    @Test
    public void test_pawnMove_promotion(){
        Game game =  FENDecoder.loadGame("3b3k/2P5/8/8/4P3/8/PP1P1PPP/R3K2R w KQ - 0 1 ");

        Move move = null;
        String encodedMove = null;

        move = game.getMove(Square.c7, Square.c8, Piece.ROOK_WHITE);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("c8R", encodedMove);

        move = game.getMove(Square.c7, Square.d8, Piece.QUEEN_WHITE);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("cxd8Q", encodedMove);
    }

    @Test
    public void test_castling(){
        Game game =  FENDecoder.loadGame("3b3k/2P5/8/8/4P3/8/PP1P1PPP/R3K2R w KQ - 0 1 ");

        Move move = null;
        String encodedMove = null;

        move = game.getMove(Square.e1, Square.c1);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("O-O-O", encodedMove);

        move = game.getMove(Square.e1, Square.g1);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("O-O", encodedMove);
    }

}
