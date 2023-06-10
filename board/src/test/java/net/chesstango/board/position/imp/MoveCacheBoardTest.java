package net.chesstango.board.position.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.MoveCacheBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
/**
 * @author Mauricio Coria
 *
 */
public class MoveCacheBoardTest {

	private MoveCacheBoard cache;
	
	private MoveFactory moveFactoryImp;
	
	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryWhite();
		cache = new MoveCacheBoardImp();
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
