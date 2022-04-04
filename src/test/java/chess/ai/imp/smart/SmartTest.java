/**
 * 
 */
package chess.ai.imp.smart;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import chess.board.Game;
import chess.board.Piece;
import chess.board.Square;
import chess.board.builder.imp.GameBuilder;
import chess.board.debug.builder.DebugChessFactory;
import chess.board.fen.FENDecoder;
import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class SmartTest {

	private Smart smart = null;
	
	@Before
	public void setUp() throws Exception {
		smart = new Smart();
	}
	
	@Test
	public void testFindBestMove1() {
		Game game =  getGame("rnbqkbnr/2pppppp/8/pp4N1/8/4PQ2/PPPP1PPP/RNB1KB1R w KQkq - 0 5");
		
		Move smartMove = smart.findBestMove(game);

		Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getValue());
		Assert.assertEquals(Square.f3, smartMove.getFrom().getKey());
		Assert.assertEquals(Square.f7, smartMove.getTo().getKey());
	}
	
	@Test
	public void testFindBestMove2() {
		Game game =  getGame("rnb1kb1r/pppp1ppp/4pq2/PN6/1P4n1/8/2PPPPPP/R1BQKBNR b KQkq - 0 7");
		
		Move smartMove = smart.findBestMove(game);

		Assert.assertEquals(Piece.QUEEN_BLACK, smartMove.getFrom().getValue());
		Assert.assertEquals(Square.f6, smartMove.getFrom().getKey());
		Assert.assertEquals(Square.f2, smartMove.getTo().getKey());
	}
	
	@Test
	public void testFindBestMove3() {
		Game game =  getGame("1k6/6R1/7Q/8/2KP3P/5P2/4P1P1/1N3BNR w - - 0 40");
		
		Move smartMove = smart.findBestMove(game);

		Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getValue());
		Assert.assertEquals(Square.h6, smartMove.getFrom().getKey());
		Assert.assertEquals(Square.h8, smartMove.getTo().getKey());
	}	
	
	
	@Test
	public void testMinValue() {
		int minValue = Integer.MIN_VALUE + 1;
		
		Assert.assertTrue((minValue * -1 )> 0 );
	}
	
	
	private Game getGame(String string) {		
		GameBuilder builder = new GameBuilder(new DebugChessFactory());

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(string);
		
		return builder.getResult();
	}	

}
