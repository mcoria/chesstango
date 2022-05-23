/**
 * 
 */
package chess.board.debug.chess;

import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.position.imp.MoveCacheBoard;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;
import chess.board.pseudomovesgenerators.imp.MoveGenaratorWithCacheProxy;
import chess.board.pseudomovesgenerators.imp.MoveGeneratorImp;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGenaratorWithCacheDebug extends MoveGenaratorWithCacheProxy {


	public MoveGenaratorWithCacheDebug(MoveGeneratorImp moveGenerator, MoveCacheBoard moveCacheBoard) {
		super(moveGenerator, moveCacheBoard);
	}


	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned origen) {
		Square origenSquare = origen.getKey();
		
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
