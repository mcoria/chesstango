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
public class MoveGenaratorWithCache implements MoveGenerator {
	protected MoveGenerator implementation = null;
	
	protected MoveCacheBoard moveCache = null;
	

	public MoveGenaratorWithCache(MoveGenerator implementation, MoveCacheBoard moveCache) {
		this.implementation = implementation;
		this.moveCache = moveCache;
	}

	@Override
	public Collection<Move> generatoPawnPasantePseudoMoves() {
		return implementation.generatoPawnPasantePseudoMoves();
	}


	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen) {
		Square origenSquare = origen.getKey();
		
		MoveGeneratorResult generatorResult = moveCache.getPseudoMovesResult(origenSquare);
		
		if (generatorResult == null) {
			
			generatorResult = implementation.generatePseudoMoves(origen);
	
			moveCache.setPseudoMoves(origenSquare, generatorResult);
		}
		
		return generatorResult;
	}

}
