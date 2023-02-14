
 package net.chesstango.search.smart;

 import net.chesstango.evaluation.GameEvaluator;
 import net.chesstango.board.Game;
 import net.chesstango.board.Piece;
 import net.chesstango.board.Square;
 import net.chesstango.board.moves.Move;
 import net.chesstango.board.representations.fen.FENDecoder;
 import org.junit.Assert;
 import org.junit.Before;
 import org.junit.Test;

 /**
  * @author Mauricio Coria
  *
  */
 public class MateIn4Test {

     private AbstractSmart bestMoveFinder = null;

     @Before
     public void setUp() {
         bestMoveFinder = new MinMaxPruning();
     }

     @Test //Viktor Korchnoi vs. Peterson
     public void test1() {
         Game game =  FENDecoder.loadGame("r2r1n2/pp2bk2/2p1p2p/3q4/3PN1QP/2P3R1/P4PP1/5RK1 w - - 0 1");

         Move smartMove = bestMoveFinder.searchBestMove(game, 7);

         Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.g4, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.g7, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }


     @Test //Paul Keres vs. Arturo Pomar Salamanca
     public void test2() {
         Game game =  FENDecoder.loadGame("7R/r1p1q1pp/3k4/1p1n1Q2/3N4/8/1PP2PPP/2B3K1 w - - 1 0");

         Move smartMove = bestMoveFinder.searchBestMove(game, 7);

         Assert.assertEquals(Piece.ROOK_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.h8, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.d8, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }

     @Test //Alexander Meek vs. Paul Morphy
     public void test3() {
         Game game =  FENDecoder.loadGame("Q7/p1p1q1pk/3p2rp/4n3/3bP3/7b/PP3PPK/R1B2R2 b - - 0 1");

         Move smartMove = bestMoveFinder.searchBestMove(game, 7);

         Assert.assertEquals(Piece.BISHOP_BLACK, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.h3, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.g2, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.BLACK_WON, bestMoveFinder.getEvaluation());
     }

 }