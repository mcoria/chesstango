/**
 * 
 */
package net.chesstango.board.debug.chess;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.movesgenerators.pseudo.imp.MoveGeneratorWithCacheProxy;
import net.chesstango.board.movesgenerators.pseudo.imp.MoveGeneratorImp;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGenaratorWithCacheDebug extends MoveGeneratorWithCacheProxy {


	public MoveGenaratorWithCacheDebug(MoveGeneratorImp moveGenerator, MoveCacheBoard moveCacheBoard) {
		super(moveGenerator, moveCacheBoard);
	}


	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen) {
		Square origenSquare = origen.getSquare();
		
		MoveGeneratorResult generatorResult = moveCache.getPseudoMovesResult(origenSquare);
		
		
		if (generatorResult == null) {
			
			generatorResult = moveGenerator.generatePseudoMoves(origen);
	
			moveCache.setPseudoMoves(origenSquare, generatorResult);
		} else {
			
			MoveGeneratorResult generatorResultActual = moveGenerator.generatePseudoMoves(origen);			

			//comparar generatorResult vs generatorResultActual
			if(generatorResultActual.getPseudoMoves().size() != generatorResult.getPseudoMoves().size()) {
				throw new RuntimeException("El cache quedó en estado inconsistente");
			}
			
			if(generatorResultActual.getAffectedByPositions() != generatorResult.getAffectedByPositions()) {
				throw new RuntimeException("AffectedBy es distinto");
			}			
		}
		
		return generatorResult;
	}	

}
