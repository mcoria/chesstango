
 package chess.ai.imp.smart;

 import chess.board.Game;
 import chess.board.Piece;
 import chess.board.Square;
 import chess.board.moves.Move;
 import org.junit.Assert;
 import org.junit.Before;
 import org.junit.Test;

 /**
  * @author Mauricio Coria
  *
  */
 public class BlackBestMovesTest extends MateInTestAbstract {

     @Override
     protected int getMaxLevel() {
         return 5;
     }

     @Before
     public void setUp() {
         super.setUp();
     }

     @Test
     public void test1() {
         // hay que sacar a la reina negra de donde esta, sino se la morfa el caballo
         Game game =  getGame("r1b1kb1r/ppp1ppp1/n2q1n2/1N1P3p/3P4/5N2/PPP2PPP/R1BQKB1R b KQkq - 2 7");

         Move smartMove = bestMoveFinder.findBestMove(game);

        System.out.println(smartMove);
     }

 }
