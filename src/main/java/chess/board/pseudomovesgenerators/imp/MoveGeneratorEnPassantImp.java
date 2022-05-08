/**
 * 
 */
package chess.board.pseudomovesgenerators.imp;

import java.util.Collection;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;
import chess.board.moves.containsers.MoveContainer;
import chess.board.moves.containsers.MovePair;
import chess.board.moves.imp.MoveFactoryBlack;
import chess.board.moves.imp.MoveFactoryWhite;
import chess.board.position.PiecePlacementReader;
import chess.board.position.imp.PositionState;
import chess.board.pseudomovesgenerators.MoveGeneratorEnPassant;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorEnPassantImp implements MoveGeneratorEnPassant {
	
	private final EnPassantMoveGeneratorBlack pasanteMoveGeneratorBlack = new EnPassantMoveGeneratorBlack();
	private final EnPassantMoveGeneratorWhite pasanteMoveGeneratorWhite = new EnPassantMoveGeneratorWhite();
	
	private PositionState positionState;
	
	private PiecePlacementReader tablero;
	

	@Override
	public MovePair generateEnPassantPseudoMoves() {
		Square pawnPasanteSquare = positionState.getEnPassantSquare();
		if (pawnPasanteSquare != null) {
			if (Color.WHITE.equals(positionState.getTurnoActual())) {
				return pasanteMoveGeneratorWhite.generatePseudoMoves(pawnPasanteSquare);
			} else {
				return pasanteMoveGeneratorBlack.generatePseudoMoves(pawnPasanteSquare);
			}
		}
		return null;
	}


	private class EnPassantMoveGeneratorBlack{
		private final MoveFactoryBlack moveFactoryImp = new MoveFactoryBlack();
		
		public MovePair generatePseudoMoves(Square pawnPasanteSquare) {
			MovePair moveContainer = new MovePair();
			PiecePositioned origen = null;
			PiecePositioned captura = null;

			Square casilleroPawnIzquirda = Square.getSquare(pawnPasanteSquare.getFile() - 1, pawnPasanteSquare.getRank() + 1);
			if(casilleroPawnIzquirda != null){
				origen = tablero.getPosicion(casilleroPawnIzquirda);
				captura = tablero.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() + 1));
				if (Piece.PAWN_BLACK.equals(origen.getValue())) {
			    	Move move = moveFactoryImp.createCaptureEnPassant(origen, tablero.getPosicion(pawnPasanteSquare), captura);
			    	moveContainer.setFirst(move);
				}
			}
			
			Square casilleroPawnDerecha = Square.getSquare(pawnPasanteSquare.getFile() + 1, pawnPasanteSquare.getRank() + 1);
			if(casilleroPawnDerecha != null){
				origen = tablero.getPosicion(casilleroPawnDerecha);
				captura = tablero.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() + 1));
				if (Piece.PAWN_BLACK.equals(origen.getValue())) {
			    	Move move = moveFactoryImp.createCaptureEnPassant(origen, tablero.getPosicion(pawnPasanteSquare), captura);
					moveContainer.setSecond(move);
				}
			}				
			
			return moveContainer;
		}
	}
	
	private class EnPassantMoveGeneratorWhite{
		private final MoveFactoryWhite moveFactoryImp = new MoveFactoryWhite();

		public MovePair generatePseudoMoves(Square pawnPasanteSquare) {
			MovePair moveContainer = new MovePair();
			PiecePositioned origen = null;
			PiecePositioned captura = null;

			Square casilleroPawnIzquirda = Square.getSquare(pawnPasanteSquare.getFile() - 1, pawnPasanteSquare.getRank() - 1);
			if(casilleroPawnIzquirda != null){
				origen = tablero.getPosicion(casilleroPawnIzquirda);
				captura = tablero.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
				if (Piece.PAWN_WHITE.equals(origen.getValue())) {
			    	Move move = this.moveFactoryImp.createCaptureEnPassant(origen, tablero.getPosicion(pawnPasanteSquare), captura);
			    	moveContainer.setFirst(move);
				}
			}
			
			Square casilleroPawnDerecha = Square.getSquare(pawnPasanteSquare.getFile() + 1, pawnPasanteSquare.getRank() - 1);
			if(casilleroPawnDerecha != null){
				origen = tablero.getPosicion(casilleroPawnDerecha);
				captura = tablero.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
				if (Piece.PAWN_WHITE.equals(origen.getValue())) {
			    	Move move = moveFactoryImp.createCaptureEnPassant(origen, tablero.getPosicion(pawnPasanteSquare), captura);
			    	moveContainer.setSecond(move);
				}
			}				
			
			return moveContainer;
		}	
	}	


	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
	}


	public void setTablero(PiecePlacementReader tablero) {
		this.tablero = tablero;
	}
	
}
