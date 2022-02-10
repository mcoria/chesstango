/**
 * 
 */
package chess.debug.chess;

import chess.PiecePositioned;
import chess.Square;
import chess.position.MoveCacheBoard;
import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.MoveGeneratorResult;
import chess.pseudomovesgenerators.imp.MoveGenaratorWithCache;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGenaratorWithCacheDebug extends MoveGenaratorWithCache {


	public MoveGenaratorWithCacheDebug(MoveGenerator moveGenerator, MoveCacheBoard moveCacheBoard) {
		super(moveGenerator, moveCacheBoard);
	}


	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen) {
		Square origenSquare = origen.getKey();
		
		MoveGeneratorResult generatorResult = moveCache.getPseudoMovesResult(origenSquare);
		
		MoveGeneratorResult generatorResultActual = implementation.generatePseudoMoves(origen);
		
		if (generatorResult == null) {
			
			generatorResult = implementation.generatePseudoMoves(origen);
	
			moveCache.setPseudoMoves(origenSquare, generatorResult);
		} else {

			//comparar generatorResult vs generatorResultActual
			if(generatorResultActual.getPseudoMoves().size() != generatorResult.getPseudoMoves().size()) {
				throw new RuntimeException("El cache quedó en estado inconsistente");
			}
			
			if(generatorResultActual.getAffectedBy() != generatorResult.getAffectedBy()) {
				throw new RuntimeException("AffectedBy es distinto");
			}			
		}
		
		return generatorResult;
	}	

}
