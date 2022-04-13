package chess.board.debug.chess;

import java.util.Collection;

import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.MoveCacheBoard;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;
import chess.board.pseudomovesgenerators.imp.MoveGeneratorImp;


/**
 * @author Mauricio Coria
 *
 */
public class MoveCacheBoardDebug extends MoveCacheBoard {

	/**
	 * @param piecePlacement
	 * @param strategy
	 */
	public MoveCacheBoardDebug() {
		super();
	}

	public void validar(PiecePlacement dummyBoard) {
		validarAffectedByAndAffects();
		
		for (PiecePositioned piecePositioned : dummyBoard) {
			if(piecePositioned.getValue() == null && pseudoMoves[piecePositioned.getKey().toIdx()] != null){
				throw new RuntimeException("En un casillero vacio de tablero existe movimeintos de cache!!!");
			}
		}
		
		for(int i = 0; i < 64; i++){
			if(pseudoMoves[i] != null && dummyBoard.isEmtpy(Square.getSquare(i))) {
				throw new RuntimeException("En un casillero de cache con movimeintos se encuentra vacio en el tablero!!!");
			}
		}
	}	


	//TODO: esta es una validacion de una propiedad emergente
	public void validar(PiecePlacement piecePlacement, MoveGeneratorImp moveGeneratorImp) {
		for(int i = 0; i < 64; i++){
			MoveGeneratorResult cacheMoveGeneratorResult = pseudoMoves[i];
			if(cacheMoveGeneratorResult != null) {
				MoveGeneratorResult expectedMoveGeneratorResults = moveGeneratorImp.generatePseudoMoves(piecePlacement.getPosicion(Square.getSquare(i)));
				compararMoveGeneratorResult(expectedMoveGeneratorResults, cacheMoveGeneratorResult);
			}
		}
	}


	private void compararMoveGeneratorResult(MoveGeneratorResult expectedMoveGeneratorResults,
			MoveGeneratorResult cacheMoveGeneratorResult) {
		
		Collection<Move> expectedPseudoMoves = expectedMoveGeneratorResults.getPseudoMoves();
		
		Collection<Move> cachePseudoMoves = cacheMoveGeneratorResult.getPseudoMoves();
		
		if(expectedPseudoMoves.size() != cachePseudoMoves.size()){
			throw new RuntimeException("Hay inconsistencia en el cache de movimientos pseudo");
		}
		
		if(expectedMoveGeneratorResults.getAffectedBy() != cacheMoveGeneratorResult.getAffectedBy()){
			throw new RuntimeException("AffectedBy no coinciden");
		}		
	}

	private void validarAffectedByAndAffects() {
		//Validate affectedBy[]
		for(int i = 0; i < 64; i++){
			if(pseudoMoves[i] != null) {
				long affectedBySquares = pseudoMoves[i].getAffectedBy();
				for(int j = 0; j < 64; j++){
					if( (affectedBySquares & (1L << j))  != 0 ) {
						if((affects[j] & (1L << i)) == 0){
							throw new RuntimeException("MoveCacheBoard checkConsistence failed");
						}
					}
				}
			}
		}
		
		//Validate affects[]
		for(int i = 0; i < 64; i++){
			long affectsSquares = affects[i];
			for(int j = 0; j < 64; j++){
				if( (affectsSquares & (1L << j)) != 0 ) {
					if((pseudoMoves[j].getAffectedBy() & (1L << i)) == 0){
						throw new RuntimeException("MoveCacheBoard checkConsistence failed");
					}
				}
			}
		}
	}	

}
