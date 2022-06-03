
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
        Game game =  getGame("r1b1kb1r/ppp1ppp1/n2q1n2/1N1P3p/3P4/5N2/PPP2PPP/R1BQKB1R b KQkq - 2 7");

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

         Assert.assertEquals(GameEvaluator.INFINITE_POSITIVE, bestMoveFinder.getEvaluation());
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

         Assert.assertEquals(GameEvaluator.INFINITE_POSITIVE, bestMoveFinder.getEvaluation());
     }


     @Test
     public void test_promotion() {
         settupMoveFinder(2);
         Game game =  getGame("4k3/8/4P3/6P1/2p2P2/PpPq1B2/7p/RNQ1K1NR b - - 1 35");

         Move smartMove = bestMoveFinder.findBestMove(game);

         System.out.println(smartMove);
         //Assert.assertEquals(Piece.KING_BLACK, smartMove.getFrom().getValue());
         //Assert.assertEquals(Square.c7, smartMove.getFrom().getKey());
         //Assert.assertEquals("There is no other option for black King", Square.b8, smartMove.getTo().getKey());
     }

     protected Game getGame(String string) {
         GameBuilder builder = new GameBuilder();

         FENDecoder parser = new FENDecoder(builder);

         parser.parseFEN(string);

         return builder.getResult();
     }

     private void settupMoveFinder(int maxLevel) {
         bestMoveFinder = new MinMaxPrunning(maxLevel);
     }
 }
