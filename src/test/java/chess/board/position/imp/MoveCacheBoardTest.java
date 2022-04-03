package chess.board.position.imp;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.position.imp.MoveCacheBoard;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;


/**
 * @author Mauricio Coria
 *
 */
public class MoveCacheBoardTest {

	private MoveCacheBoard cache;
	
	private MoveFactoryWhite moveFactoryImp;
	
	@Before
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		cache = new MoveCacheBoard();
	}
	
	@Test
	public void test01() {
		MoveGeneratorResult result = new MoveGeneratorResult();
		result.moveContainerAdd(moveFactoryImp.createSimpleMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.a3, null)));
		result.moveContainerAdd(moveFactoryImp.createSimpleMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.a4, null)));
		cache.setPseudoMoves(Square.a2, result);
		
		
		assertNotNull(cache.getPseudoMovesResult(Square.a2));
		
	}	

}
