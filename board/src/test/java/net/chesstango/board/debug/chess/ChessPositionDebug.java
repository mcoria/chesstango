package net.chesstango.board.debug.chess;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.movesgenerators.pseudo.imp.MoveGeneratorImp;
import net.chesstango.board.position.imp.ChessPositionImp;
import net.chesstango.board.representations.ascii.ASCIIEncoder;

import java.util.Collection;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionDebug extends ChessPositionImp {
	

	private MoveGeneratorImp moveGeneratorImp;


	@Override
	public void acceptForDo(Move move) {
		super.acceptForDo(move);
		((PositionStateDebug)positionState).validar(this.squareBoard);
		((ColorBoardDebug)colorBoard).validar(this.squareBoard);
		((KingSquareDebug) kingSquare).validar(this.squareBoard);
		((MoveCacheBoardDebug)moveCache).validar(this.squareBoard);
		((ZobristHashDebug)zobristHash).validar(this);
	}

	@Override
	public void acceptForUndo(Move move) {
		super.acceptForUndo(move);
		((PositionStateDebug)positionState).validar(this.squareBoard);
		((ColorBoardDebug)colorBoard).validar(this.squareBoard);
		((KingSquareDebug) kingSquare).validar(this.squareBoard);
		((MoveCacheBoardDebug)moveCache).validar(this.squareBoard);
		((ZobristHashDebug)zobristHash).validar(this);
		validar(getMoveGeneratorImp());
	}
	
	@Override
	public void init() {
		super.init();
		((PositionStateDebug)positionState).validar(this.squareBoard);
		((ColorBoardDebug)colorBoard).validar(this.squareBoard);
		((KingSquareDebug) kingSquare).validar(this.squareBoard);
		((MoveCacheBoardDebug)moveCache).validar(this.squareBoard);
	}
	
	protected MoveGeneratorImp getMoveGeneratorImp() {
		if (moveGeneratorImp == null) {
			moveGeneratorImp = new MoveGeneratorImp();			
			moveGeneratorImp.setPiecePlacement(this.squareBoard);
			moveGeneratorImp.setBoardState(this.positionState);
			moveGeneratorImp.setColorBoard(this.colorBoard);
		}
		return moveGeneratorImp;
	}
	
	
	//TODO: esta es una validacion de una propiedad emergente
	public void validar(MoveGeneratorImp moveGeneratorImp) {
		for(int i = 0; i < 64; i++){
			Square square = Square.getSquareByIdx(i);
			MoveGeneratorResult cacheMoveGeneratorResult = moveCache.getPseudoMovesResult(square);
			if(cacheMoveGeneratorResult != null) {
				MoveGeneratorResult expectedMoveGeneratorResults = moveGeneratorImp.generatePseudoMoves(squareBoard.getPosition(square));
				compararMoveGeneratorResult(expectedMoveGeneratorResults, cacheMoveGeneratorResult);
			}
		}
	}

	@Override
	public String toString() {
		ASCIIEncoder asciiEncoder = new ASCIIEncoder();
		constructChessPositionRepresentation(asciiEncoder);

		return super.toString() + "\n" + asciiEncoder.getChessRepresentation();
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