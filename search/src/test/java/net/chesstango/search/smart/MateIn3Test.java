
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
 public class MateIn3Test {

     private AbstractSmart bestMoveFinder = null;

     @Before
     public void setUp() {
         bestMoveFinder = new MinMaxPruning();
     }

     @Test //Roberto Grau vs. Edgar Colle
     public void test1() {
         Game game =  FENDecoder.loadGame("1k5r/pP3ppp/3p2b1/1BN1n3/1Q2P3/P1B5/KP3P1P/7q w - - 1 0");

         Move smartMove = bestMoveFinder.searchBestMove(game, 5);

         Assert.assertEquals(Piece.KNIGHT_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.c5, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.a6, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }

     @Test //Johannes Zukertort vs. William Norwood Potter
     public void test2() {
         Game game =  FENDecoder.loadGame("3r4/pR2N3/2pkb3/5p2/8/2B5/qP3PPP/4R1K1 w - - 1 0");

         Move smartMove = bestMoveFinder.searchBestMove(game, 5);

         Assert.assertEquals(Piece.BISHOP_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.c3, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.e5, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }

     @Test //Ossip Bernstein vs. Alexander Kotov
     public void test3() {
         Game game =  FENDecoder.loadGame("R6R/1r3pp1/4p1kp/3pP3/1r2qPP1/7P/1P1Q3K/8 w - - 1 0");

         Move smartMove = bestMoveFinder.searchBestMove(game, 5);

         Assert.assertEquals(Piece.PAWN_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.f4, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.f5, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }

     @Test //Paul Vaitonis vs. Reuben Fine
     public void test4() {
         Game game =  FENDecoder.loadGame("4r1k1/5bpp/2p5/3pr3/8/1B3pPq/PPR2P2/2R2QK1 b - - 0 1");

         Move smartMove = bestMoveFinder.searchBestMove(game, 5);

         Assert.assertEquals(Piece.ROOK_BLACK, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.e5, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.e1, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.BLACK_WON, bestMoveFinder.getEvaluation());
     }

     @Test //Géza Maróczy vs. Heinrich Wolf
     public void test5() {
         Game game =  FENDecoder.loadGame("2r5/2p2k1p/pqp1RB2/2r5/PbQ2N2/1P3PP1/2P3P1/4R2K w - - 1 0");

         Move smartMove = bestMoveFinder.searchBestMove(game, 5);

         Assert.assertEquals(Piece.ROOK_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.e6, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.e7, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }

     @Test //William Evans vs. Alexander MacDonnell
     public void test6() {
         Game game =  FENDecoder.loadGame("r3k2r/ppp2Npp/1b5n/4p2b/2B1P2q/BQP2P2/P5PP/RN5K w kq - 1 0");

         Move smartMove = bestMoveFinder.searchBestMove(game, 5);

         Assert.assertEquals(Piece.BISHOP_WHITE, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.c4, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.b5, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }

     @Test //Max Walter vs. Emanuel Lasker
     public void test7() {
         Game game =  FENDecoder.loadGame("4r1k1/3n1ppp/4r3/3n3q/Q2P4/5P2/PP2BP1P/R1B1R1K1 b - - 0 1");

         Move smartMove = bestMoveFinder.searchBestMove(game, 5);

         Assert.assertEquals(Piece.ROOK_BLACK, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.e6, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.g6, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.BLACK_WON, bestMoveFinder.getEvaluation());
     }
 }
