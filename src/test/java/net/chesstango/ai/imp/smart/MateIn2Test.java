
 package net.chesstango.ai.imp.smart;

 import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;
 import net.chesstango.ai.imp.smart.evaluation.imp.GameEvaluatorBasic;
 import net.chesstango.board.Game;
 import net.chesstango.board.Piece;
 import net.chesstango.board.Square;
 import net.chesstango.board.moves.Move;
 import net.chesstango.board.moves.MovePromotion;
 import net.chesstango.board.representations.fen.FENDecoder;
 import org.junit.Assert;
 import org.junit.Before;
 import org.junit.Test;

 /**
  * @author Mauricio Coria
  *
  */
 public class MateIn2Test {

     private AbstractSmart bestMoveFinder = null;

     @Before
     public void setUp() {
         bestMoveFinder = new MinMaxPruning(new GameEvaluatorBasic());
     }

     @Test
     public void testQueenMove1() {
         Game game =  FENDecoder.loadGame("5K2/1b6/2p1Bp2/2N1k3/1B3p2/2pQ2n1/1b2pp2/8 w - - 0 1");

         Move smartMove = bestMoveFinder.searchBestMove(game, 3);

         Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.d3, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.c4, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }

     @Test
     public void testQueenMove2() {
         Game game =  FENDecoder.loadGame("8/8/5p2/1R1Nk3/R7/7K/8/1Q6 w - - 0 1");

         Move smartMove = bestMoveFinder.searchBestMove(game, 3);

         Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.b1, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.f5, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }

     @Test
     public void testKnightMove1() {
         Game game =  FENDecoder.loadGame("r2qkb1r/pp2nppp/3p4/2pNN1B1/2BnP3/3P4/PPP2PPP/R2bK2R w KQkq - 1 1");

         Move smartMove = bestMoveFinder.searchBestMove(game, 3);

         Assert.assertEquals(Piece.KNIGHT_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.d5, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.f6, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }

     //Robert Thacker vs. Bobby Fischer
     @Test
     public void testPromotion() {
         Game game =  FENDecoder.loadGame("2r2r2/6kp/3p4/3P4/4Pp2/5P1P/PP1pq1P1/4R2K b - - 0 1");

         Move smartMove = bestMoveFinder.searchBestMove(game, 3);

         Assert.assertEquals(Piece.PAWN_BLACK, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.d2, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.e1, smartMove.getTo().getKey());

         Assert.assertTrue(smartMove instanceof MovePromotion);
         Assert.assertEquals(Piece.KNIGHT_BLACK, ((MovePromotion)smartMove).getPromotion());

         Assert.assertEquals(GameEvaluator.BLACK_WON, bestMoveFinder.getEvaluation());
     }

 }
