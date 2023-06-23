package net.chesstango.board.movesgenerators.legal.filters;

import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.moves.impl.bridge.MovePawnCaptureEnPassant;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.legal.squarecapturers.CardinalSquareCaptured;
import net.chesstango.board.movesgenerators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.imp.KingSquareImp;

/**
 * Este filtro se utiliza cuando el jugador actual no se encuentra en jaque
 *
 * @author Mauricio Coria
 *
 */
public class NoCheckMoveFilter implements MoveFilter {
	
	protected final SquareBoard dummySquareBoard;
	protected final KingSquareImp kingCacheBoard;
	protected final BitBoard bitBoard;
	protected final PositionStateReader positionState;

	protected final FullScanSquareCaptured fullScanSquareCapturer;
	protected final CardinalSquareCaptured cardinalSquareCapturer;
	
	public NoCheckMoveFilter(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard, PositionStateReader positionState) {
		this.dummySquareBoard = dummySquareBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.bitBoard = bitBoard;
		this.positionState = positionState;
		this.fullScanSquareCapturer = new FullScanSquareCaptured(dummySquareBoard, bitBoard);
		this.cardinalSquareCapturer = new CardinalSquareCaptured(dummySquareBoard, bitBoard);
	}
	
	@Override
	//TODO: deberiamos crear un filtro especifico para EnPassant?
	//      solo movimientos EnPassant terminan siendo filtrados aca,
	//      el resto de los movimiento notking son filtrados por el NoCheckLegalMoveGenerator
	/**
	 *  Este metodo sirve para filtrar movimientos que no son de rey.
	 *  Dado que no se encuentra en jaque, no pregunta por jaque de knight; king o pawn
	 */
	public boolean filterMove(Move move) {
		if( ! (move instanceof MovePawnCaptureEnPassant)){
			throw new RuntimeException("Solo deberiamos filtrar MovePawnCaptureEnPassant");
		}

		boolean result = false;
		
		final Color turnoActual = positionState.getCurrentTurn();
		final Color opositeTurnoActual = turnoActual.oppositeColor();
		
		move.executeMove(this.dummySquareBoard);
		move.executeMove(this.bitBoard);

		if(! cardinalSquareCapturer.isCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
			result = true;
		}

		move.undoMove(this.bitBoard);
		move.undoMove(this.dummySquareBoard);

		return result;
	}
	
	@Override
	public boolean filterMoveKing(MoveKing move) {
		boolean result = false;
		final Color turnoActual = positionState.getCurrentTurn();
		final Color opositeTurnoActual = turnoActual.oppositeColor();
		
		move.executeMove(this.kingCacheBoard);
		move.executeMove(this.dummySquareBoard);
		move.executeMove(this.bitBoard);

		if(! fullScanSquareCapturer.isCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
			result = true;
		}

		move.undoMove(this.bitBoard);
		move.undoMove(this.dummySquareBoard);
		move.undoMove(this.kingCacheBoard);
		
		return result;
	}

	//TODO: este metodo esta consumiendo el 20% del procesamiento,
	// 		deberia crear CAPTURER especifico para validar castling
	@Override
	public boolean filterMoveCastling(MoveCastling moveCastling) {
		Color opositeColor = moveCastling.getFrom().getPiece().getColor().oppositeColor();
		//assert(!capturer.positionCaptured(oppositeColor, moveCastling.getFrom().getKey())); 					    // El king no esta en jaque... lo asumimos
		return !fullScanSquareCapturer.isCaptured(opositeColor, moveCastling.getRookTo().getSquare()) 		// El king no puede ser capturado en casillero intermedio
			&& !fullScanSquareCapturer.isCaptured(opositeColor, moveCastling.getTo().getSquare());  			// El king no puede  ser capturado en casillero destino
		
	}		

}
