package net.chesstango.board.internal.moves.generators.pseudo;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.position.MoveCacheBoard;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorCacheDebug extends MoveGeneratorCache {


	public MoveGeneratorCacheDebug(MoveGenerator moveGenerator, MoveCacheBoard moveCacheBoard) {
		super(moveGenerator, moveCacheBoard);
	}


	@Override
	public MoveGeneratorByPieceResult generateByPiecePseudoMoves(PiecePositioned from) {
		Square origenSquare = from.getSquare();
		
		MoveGeneratorByPieceResult generatorResult = moveCache.getPseudoMovesResult(origenSquare);
		
		
		if (generatorResult == null) {
			
			generatorResult = moveGenerator.generateByPiecePseudoMoves(from);
	
			moveCache.setPseudoMoves(origenSquare, generatorResult);
		} else {
			
			MoveGeneratorByPieceResult generatorResultActual = moveGenerator.generateByPiecePseudoMoves(from);

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
