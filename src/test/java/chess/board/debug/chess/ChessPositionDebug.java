package chess.board.debug.chess;

import java.util.Collection;

import chess.board.Square;
import chess.board.moves.Move;
import chess.board.position.imp.ChessPositionImp;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;
import chess.board.pseudomovesgenerators.imp.MoveGeneratorImp;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionDebug extends ChessPositionImp {
	

	private MoveGeneratorImp moveGeneratorImp;


	@Override
	public void acceptForExecute(Move move) {
		super.acceptForExecute(move);
		((PositionStateDebug)positionState).validar(this.piecePlacement);
		((ColorBoardDebug)colorBoard).validar(this.piecePlacement);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.piecePlacement);
		((MoveCacheBoardDebug)moveCache).validar(this.piecePlacement);
	}

	@Override
	public void acceptForUndo(Move move) {
		super.acceptForUndo(move);
		((PositionStateDebug)positionState).validar(this.piecePlacement);
		((ColorBoardDebug)colorBoard).validar(this.piecePlacement);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.piecePlacement);
		((MoveCacheBoardDebug)moveCache).validar(this.piecePlacement);
		validar(getMoveGeneratorImp());
	}
	
	@Override
	public void init() {
		super.init();
		((PositionStateDebug)positionState).validar(this.piecePlacement);
		((ColorBoardDebug)colorBoard).validar(this.piecePlacement);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.piecePlacement);
		((MoveCacheBoardDebug)moveCache).validar(this.piecePlacement);			
	}
	
	protected MoveGeneratorImp getMoveGeneratorImp() {
		if (moveGeneratorImp == null) {
			moveGeneratorImp = new MoveGeneratorImp();			
			moveGeneratorImp.setPiecePlacement(this.piecePlacement);
			moveGeneratorImp.setBoardState(this.positionState);
			moveGeneratorImp.setColorBoard(this.colorBoard);
		}
		return moveGeneratorImp;
	}
	
	
	//TODO: esta es una validacion de una propiedad emergente
	public void validar(MoveGeneratorImp moveGeneratorImp) {
		for(int i = 0; i < 64; i++){
			Square square = Square.getSquare(i);
			MoveGeneratorResult cacheMoveGeneratorResult = moveCache.getPseudoMovesResult(square);
			if(cacheMoveGeneratorResult != null) {
				MoveGeneratorResult expectedMoveGeneratorResults = moveGeneratorImp.generatePseudoMoves(piecePlacement.getPosicion(square));
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
		
		if(expectedMoveGeneratorResults.getAffectedByPositions() != cacheMoveGeneratorResult.getAffectedByPositions()){
			throw new RuntimeException("AffectedBy no coinciden");
		}		
	}	

}