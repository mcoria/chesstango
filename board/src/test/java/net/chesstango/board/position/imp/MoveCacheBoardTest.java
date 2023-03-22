package net.chesstango.board.position.imp;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.imp.MoveFactoryWhite;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;


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
		MoveGeneratorResult result = new MoveGeneratorResult(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE));
		result.addPseudoMove(moveFactoryImp.createSimpleMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.a3, null)));
		result.addPseudoMove(moveFactoryImp.createSimpleMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.a4, null)));
		cache.setPseudoMoves(Square.a2, result);
		
		
		assertNotNull(cache.getPseudoMovesResult(Square.a2));
		
	}	

}
