
 package chess.ai.imp.smart;

 import chess.ai.BestMoveFinder;
 import chess.board.Game;
 import chess.board.Piece;
 import chess.board.Square;
 import chess.board.builder.imp.GameBuilder;
 import chess.board.moves.Move;
 import chess.board.representations.fen.FENDecoder;
 import org.junit.Assert;
 import org.junit.Before;
 import org.junit.Test;

 /**
  * @author Mauricio Coria
  *
  */
 public class BlackBestMovesTest{

     protected BestMoveFinder bestMoveFinder = null;


     @Test
     public void test_moveQueen() {
        settupMoveFinder(2);
        // hay que sacar a la reina negra de donde esta, sino se la morfa el caballo
        Game game =  getGame("r1b1kb1r/ppp1ppp1/n2q1n2/1N1P3p/3P4/5N2/PPP2PPP/R1BQKB1R b KQkq - 1 1");

        Move smartMove = bestMoveFinder.findBestMove(game);

        Assert.assertEquals(Piece.QUEEN_BLACK, smartMove.getFrom().getValue());
        Assert.assertEquals(Square.d6, smartMove.getFrom().getKey());
        Assert.assertNotEquals("Queen captured by pawn", Square.a3, smartMove.getTo().getKey());
        Assert.assertNotEquals("Queen captured by pawn", Square.c5, smartMove.getTo().getKey());
        Assert.assertNotEquals("Queen captured by pawn", Square.c6, smartMove.getTo().getKey());
        Assert.assertNotEquals("Queen captured by pawn", Square.e6, smartMove.getTo().getKey());
        Assert.assertNotEquals("Queen captured by bishop", Square.f4, smartMove.getTo().getKey());
        Assert.assertNotEquals("Queen captured by pawn", Square.g3, smartMove.getTo().getKey());
     }

     @Test
     public void test_imminentMateIn2Moves() {
         settupMoveFinder(2);
         // Black will be in checkmate in the next 1 move
         Game game =  getGame("8/2kQ2P1/8/1pP5/8/1B3P2/3R4/6K1 b - - 1 1");

         Move smartMove = bestMoveFinder.findBestMove(game);

         Assert.assertEquals(Piece.KING_BLACK, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.c7, smartMove.getFrom().getKey());
         Assert.assertEquals("There is no other option for black King", Square.b8, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }


     @Test
     public void test_imminentMateIn4Moves() {
         settupMoveFinder(4);
         // Black will be in checkmate in the next 2 move
         Game game =  getGame("8/2kQ4/6P1/1pP5/8/1B3P2/3R4/6K1 b - - 1 1");

         Move smartMove = bestMoveFinder.findBestMove(game);

         Assert.assertEquals(Piece.KING_BLACK, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.c7, smartMove.getFrom().getKey());
         Assert.assertEquals("There is no other option for black King", Square.b8, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
     }

     //5R2/6p1/2p1pp2/3p4/K1k5/8/8/1q6 b - - 2 55

     @Test
     public void test_Mate() {
         settupMoveFinder(5);
         // Black can win the game in the next move
         Game game =  getGame("5R2/6p1/2p1pp2/3p4/K1k5/8/8/1q6 b - - 1 1");

         Move smartMove = bestMoveFinder.findBestMove(game);

         Assert.assertEquals(Piece.QUEEN_BLACK, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.b1, smartMove.getFrom().getKey());

         Square to = smartMove.getTo().getKey();
         Assert.assertTrue(Square.b4.equals(to) || Square.a2.equals(to) );

         Assert.assertEquals(GameEvaluator.BLACK_WON, bestMoveFinder.getEvaluation());
     }

     @Test //Max Walter vs. Emanuel Lasker
     public void test_MateInTwo() {
         settupMoveFinder(3);

         Game game =  getGame("4r1k1/3n1ppp/4r3/3n3q/Q2P4/5P2/PP2BP1P/R1B1R1K1 b - - 0 1");

         Move smartMove = bestMoveFinder.findBestMove(game);

         Assert.assertEquals(Piece.ROOK_BLACK, smartMove.getFrom().getValue());
         Assert.assertEquals(Square.e6, smartMove.getFrom().getKey());
         Assert.assertEquals(Square.g6, smartMove.getTo().getKey());

         Assert.assertEquals(GameEvaluator.BLACK_WON, bestMoveFinder.getEvaluation());
     }

     protected Game getGame(String string) {
         GameBuilder builder = new GameBuilder();

         FENDecoder parser = new FENDecoder(builder);

         parser.parseFEN(string);

         return builder.getResult();
     }

     private void settupMoveFinder(int maxLevel) {
         //bestMoveFinder = new MinMaxPrunning(maxLevel);
         bestMoveFinder = new SmartLoop(maxLevel);
     }
 }