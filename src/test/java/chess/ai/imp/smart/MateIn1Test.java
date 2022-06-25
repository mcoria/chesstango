 package chess.ai.imp.smart;

 import chess.ai.imp.smart.evaluation.GameEvaluator;
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
public class MateIn1Test extends MateInTestAbstract {

	@Override
	protected int getMaxLevel() {
		return 1;
	}

	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void testQueenWhiteCheckMate() {
		// Jaque Mate en movimiento de QUEEN_WHITE
		Game game =  FENDecoder.loadGame("rnbqkbnr/2pppppp/8/pp4N1/8/4PQ2/PPPP1PPP/RNB1KB1R w KQkq - 0 5");
		
		Move smartMove = bestMoveFinder.searchBestMove(game);

		Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getValue());
		Assert.assertEquals(Square.f3, smartMove.getFrom().getKey());
		Assert.assertEquals(Square.f7, smartMove.getTo().getKey());

		Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
	}
	
	@Test
	public void testQueenBlackCheckMate() {
		// Jaque Mate en movimiento de QUEEN_BLACK		
		Game game =  FENDecoder.loadGame("rnb1kb1r/pppp1ppp/4pq2/PN6/1P4n1/8/2PPPPPP/R1BQKBNR b KQkq - 0 7");
		
		Move smartMove = bestMoveFinder.searchBestMove(game);

		Assert.assertEquals(Piece.QUEEN_BLACK, smartMove.getFrom().getValue());
		Assert.assertEquals(Square.f6, smartMove.getFrom().getKey());
		Assert.assertEquals(Square.f2, smartMove.getTo().getKey());

		Assert.assertEquals(GameEvaluator.BLACK_WON, bestMoveFinder.getEvaluation());
	}
	
	@Test
	public void testKingTrapped() {
		// Jaque Mate en movimiento de QUEEN_WHITE	(rey esta solo y atrapado por torre blanca)
		Game game =  FENDecoder.loadGame("1k6/6R1/7Q/8/2KP3P/5P2/4P1P1/1N3BNR w - - 0 40");
		
		Move smartMove = bestMoveFinder.searchBestMove(game);

		Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getValue());
		Assert.assertEquals(Square.h6, smartMove.getFrom().getKey());
		Assert.assertEquals(Square.h8, smartMove.getTo().getKey());

		Assert.assertEquals(GameEvaluator.WHITE_WON, bestMoveFinder.getEvaluation());
	}	

	
	@Test
	public void testFoolsMateTest() {
		// Fool's mate
		Game game =  FENDecoder.loadGame("rnbqkbnr/pppp1ppp/4p3/8/6P1/5P2/PPPPP2P/RNBQKBNR b KQkq - 0 2");
		
		Move smartMove = bestMoveFinder.searchBestMove(game);

		Assert.assertEquals(Piece.QUEEN_BLACK, smartMove.getFrom().getValue());
		Assert.assertEquals(Square.d8, smartMove.getFrom().getKey());
		Assert.assertEquals(Square.h4, smartMove.getTo().getKey());

		Assert.assertEquals(GameEvaluator.BLACK_WON, bestMoveFinder.getEvaluation());
	}	
	
}
