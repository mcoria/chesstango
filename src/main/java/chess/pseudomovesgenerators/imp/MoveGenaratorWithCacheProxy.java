/**
 * 
 */
package chess.pseudomovesgenerators.imp;

import java.util.Collection;

import chess.PiecePositioned;
import chess.Square;
import chess.moves.Move;
import chess.position.MoveCacheBoard;
import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.MoveGeneratorResult;

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
	public Collection<Move> generatoPawnPasantePseudoMoves() {
		return moveGenerator.generatoPawnPasantePseudoMoves();
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

}
