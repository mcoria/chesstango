package chess.position;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.moves.MoveFactory;
import chess.position.MoveCacheBoard;
import chess.pseudomovesgenerators.MoveGeneratorResult;


/**
 * @author Mauricio Coria
 *
 */
public class MoveCacheBoardTest {

	private MoveCacheBoard cache;
	
	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		cache = new MoveCacheBoard();
	}
	
	@Test
	public void test01() {
		MoveGeneratorResult result = new MoveGeneratorResult();
		result.moveContainerAdd(moveFactory.createSimpleMove(new PiecePositioned(Square.a2, Piece.PAWN_WHITE), new PiecePositioned(Square.a3, null)));
		result.moveContainerAdd(moveFactory.createSimpleMove(new PiecePositioned(Square.a2, Piece.PAWN_WHITE), new PiecePositioned(Square.a4, null)));
		cache.setPseudoMoves(Square.a2, result);
		
		
		assertNotNull(cache.getPseudoMoves(Square.a2));
		
	}	

}