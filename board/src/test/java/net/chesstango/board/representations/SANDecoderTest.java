package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.Assert;
import org.junit.Test;

public class SANDecoderTest {

    private SANDecoder decoder = new SANDecoder();

    @Test
    public void test_knight_move01(){
        Game game =  FENDecoder.loadGame("rnbqk2r/pp1p1ppp/4pn2/2p5/1bPP4/2N1P3/PP3PPP/R1BQKBNR w KQkq c6 0 5");

        Move expectedMove = game.getMove(Square.g1, Square.e2);
        Move decodedMove = decoder.decode("Nge2", game.getPossibleMoves());

        Assert.assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_knight_move02(){
        Game game =  FENDecoder.loadGame("rnbqk2r/pp1p1ppp/4pn2/2p5/1bPP4/2N1P3/PP3PPP/R1BQKBNR w KQkq c6 0 5");

        Move expectedMove = game.getMove(Square.g1, Square.f3);
        Move decodedMove = decoder.decode("Nf3", game.getPossibleMoves());

        Assert.assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_bishop(){
        Game game =  FENDecoder.loadGame("rnbqk2r/pp1p1ppp/4pn2/2p5/1bPP4/2N1P3/PP3PPP/R1BQKBNR w KQkq c6 0 5");

        Move expectedMove = game.getMove(Square.c1, Square.d2);
        Move decodedMove = decoder.decode("Bd2", game.getPossibleMoves());

        Assert.assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_pawn_push01(){
        Game game =  FENDecoder.loadGame("rnbqk2r/pp1p1ppp/4pn2/2p5/1bPP4/2N1P3/PP3PPP/R1BQKBNR w KQkq c6 0 5");

        Move expectedMove = game.getMove(Square.a2, Square.a3);
        Move decodedMove = decoder.decode("a3", game.getPossibleMoves());

        Assert.assertEquals(expectedMove, decodedMove);
    }
}
