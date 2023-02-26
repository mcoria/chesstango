/**
 * 
 */
package net.chesstango.board.movesgenerators.pseudo.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorWithCacheProxy implements MoveGenerator {

	protected MoveGeneratorImp moveGenerator = null;
	
	protected MoveCacheBoard moveCache = null;
	

	public MoveGeneratorWithCacheProxy(MoveGeneratorImp implementation, MoveCacheBoard moveCache) {
		this.moveGenerator = implementation;
		this.moveCache = moveCache;
	}	


	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen) {
		Square origenSquare = origen.getSquare();
		
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