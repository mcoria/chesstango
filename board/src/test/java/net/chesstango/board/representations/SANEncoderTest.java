package net.chesstango.board.representations;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
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
        Game game =  FENDecoder.loadGame("3b3k/2P5/8/8/4P3/8/PP1P1PPP/R3K2R w KQ - 0 1");

        Move move = null;
        String encodedMove = null;

        move = game.getMove(Square.c7, Square.c8, Piece.ROOK_WHITE);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("c8=R", encodedMove);

        move = game.getMove(Square.c7, Square.d8, Piece.QUEEN_WHITE);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("cxd8=Q", encodedMove);
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

    @Test
    public void test_knight_move01(){
        Game game =  FENDecoder.loadGame("r1k4r/ppp4p/2nb1pq1/3p1np1/4p1Q1/4P3/PPPPNPPP/RNB1K2R w KQ - 0 1");

        Move move = null;
        String encodedMove = null;

        move = game.getMove(Square.b1, Square.c3);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("Nbc3", encodedMove);
    }

    @Test
    public void test_knight_move02(){
        Game game =  FENDecoder.loadGame("rk2q3/ppp5/6n1/2b5/4pp2/P1N5/1PPPKPRP/R1B5 b - - 19 47");

        Move move = null;
        String encodedMove = null;

        move = game.getMove(Square.g6, Square.h4);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("Nh4", encodedMove);
    }


    @Test
    public void test_knight_move03(){
        Game game =  FENDecoder.loadGame("rk2q3/ppp5/5p2/2b2np1/4p3/P1N1Pn2/1PPPKPRP/R1B5 b - - 3 34");

        Move move = null;
        String encodedMove = null;

        move = game.getMove(Square.f3, Square.h4);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("N3h4", encodedMove);

        move = game.getMove(Square.f5, Square.h4);
        encodedMove = encoder.encode(move, game.getPossibleMoves());
        Assert.assertEquals("N5h4", encodedMove);
    }

}
