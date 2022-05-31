
 package chess.ai.imp.smart;

 import chess.board.Game;
 import chess.board.Piece;
 import chess.board.Square;
 import chess.board.moves.Move;
 import chess.board.representations.fen.FENDecoder;
 import org.junit.Assert;
 import org.junit.Before;
 import org.junit.Test;

 /**
  * @author Mauricio Coria
  *
  */
 public class MateIn4Test extends MateInTestAbstract {

     @Override
     protected int getMaxLevel() {
         return 7;
     }

     @Before
     public void setUp() {
         super.setUp();
     }

     @Test //Viktor Korchnoi vs. Peterson
     public void test1() {
         Game game =  getGame("r2r1n2/pp2bk2/2p1p2p/3q4/3PN1QP/2P3R1/P4PP1/5RK1 w - - 0 1");

         Move smartMove = bestMoveFinder.findBestMove(game);

         Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.g4, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.g7, smartMove.getTo().getKey());
     }


     @Test //Paul Keres vs. Arturo Pomar Salamanca
     public void test2() {
         Game game =  getGame("7R/r1p1q1pp/3k4/1p1n1Q2/3N4/8/1PP2PPP/2B3K1 w - - 1 0");

         Move smartMove = bestMoveFinder.findBestMove(game);

         Assert.assertEquals(Piece.ROOK_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.h8, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.d8, smartMove.getTo().getKey());
     }

     @Test
     public void test3() {
         Game game =  getGame(FENDecoder.INITIAL_FEN);

         Move smartMove = bestMoveFinder.findBestMove(game);

         System.out.println(smartMove);

     }
 }
