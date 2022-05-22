/**
 * 
 */
package chess.board.pseudomovesgenerators.imp;

import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.containers.MovePair;
import chess.board.position.imp.MoveCacheBoard;
import chess.board.pseudomovesgenerators.MoveGenerator;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGenaratorWithCacheProxy implements MoveGenerator {

	protected MoveGeneratorImp moveGenerator = null;
	
	protected MoveCacheBoard moveCache = null;
	

	public MoveGenaratorWithCacheProxy(MoveGeneratorImp implementation, MoveCacheBoard moveCache) {
		this.moveGenerator = implementation;
		this.moveCache = moveCache;
	}	


	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen) {
		Square origenSquare = origen.getKey();
		
		MoveGeneratorResult generatorResult = moveCache.getPseudoMovesResult(origenSquare);
		
		if (generatorResult == null) {
			
			generatorResult = moveGenerator.generatePseudoMoves(origen);
	
			moveCache.setPseudoMoves(origenSquare, generatorResult);
		}
		
		return generatorResult;
	}
	
	@Override
	public MovePair generateEnPassantPseudoMoves() {
		return moveGenerator.generateEnPassantPseudoMoves();
	}
	
	@Override
	public MovePair generateCastlingPseudoMoves() {
		return moveGenerator.generateCastlingPseudoMoves();
	}	

}
