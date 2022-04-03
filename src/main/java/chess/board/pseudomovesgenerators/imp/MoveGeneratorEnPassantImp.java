/**
 * 
 */
package chess.board.pseudomovesgenerators.imp;

import java.util.ArrayList;
import java.util.Collection;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;
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
	
	private EnPassantMoveGeneratorBlack pasanteMoveGeneratorBlack = new EnPassantMoveGeneratorBlack();
	private EnPassantMoveGeneratorWhite pasanteMoveGeneratorWhite = new EnPassantMoveGeneratorWhite();
	
	private PositionState positionState;
	
	private PiecePlacementReader tablero;
	

	@Override
	public Collection<Move> generateEnPassantPseudoMoves() {
		Collection<Move> moveContainer = createContainer();
		Square pawnPasanteSquare = positionState.getEnPassantSquare();
		if (pawnPasanteSquare != null) {
			if (Color.WHITE.equals(positionState.getTurnoActual())) {
				return pasanteMoveGeneratorWhite.generatePseudoMoves(moveContainer, pawnPasanteSquare);
			} else {
				return pasanteMoveGeneratorBlack.generatePseudoMoves(moveContainer, pawnPasanteSquare);
			}
		}
		return moveContainer;
	}


	private class EnPassantMoveGeneratorBlack{
		private MoveFactoryBlack moveFactoryImp = new MoveFactoryBlack();
		
		public Collection<Move> generatePseudoMoves(Collection<Move> moveContainer, Square pawnPasanteSquare) {
			PiecePositioned origen = null;
			PiecePositioned captura = null;

			Square casilleroPawnIzquirda = Square.getSquare(pawnPasanteSquare.getFile() - 1, pawnPasanteSquare.getRank() + 1);
			if(casilleroPawnIzquirda != null){
				origen = tablero.getPosicion(casilleroPawnIzquirda);
				captura = tablero.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() + 1));
				if (Piece.PAWN_BLACK.equals(origen.getValue())) {
			    	Move move = moveFactoryImp.createCaptureEnPassant(origen, tablero.getPosicion(pawnPasanteSquare), captura);
			    	moveContainer.add(move);
				}
			}
			
			Square casilleroPawnDerecha = Square.getSquare(pawnPasanteSquare.getFile() + 1, pawnPasanteSquare.getRank() + 1);
			if(casilleroPawnDerecha != null){
				origen = tablero.getPosicion(casilleroPawnDerecha);
				captura = tablero.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() + 1));
				if (Piece.PAWN_BLACK.equals(origen.getValue())) {
			    	Move move = moveFactoryImp.createCaptureEnPassant(origen, tablero.getPosicion(pawnPasanteSquare), captura);
			    	moveContainer.add(move);
				}
			}				
			
			return moveContainer;
		}
	}
	
	private class EnPassantMoveGeneratorWhite{
		private MoveFactoryWhite moveFactoryImp = new MoveFactoryWhite();		

		public Collection<Move> generatePseudoMoves(Collection<Move> moveContainer, Square pawnPasanteSquare) {
			PiecePositioned origen = null;
			PiecePositioned captura = null;

			Square casilleroPawnIzquirda = Square.getSquare(pawnPasanteSquare.getFile() - 1, pawnPasanteSquare.getRank() - 1);
			if(casilleroPawnIzquirda != null){
				origen = tablero.getPosicion(casilleroPawnIzquirda);
				captura = tablero.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
				if (Piece.PAWN_WHITE.equals(origen.getValue())) {
			    	Move move = this.moveFactoryImp.createCaptureEnPassant(origen, tablero.getPosicion(pawnPasanteSquare), captura);
			    	moveContainer.add(move);
				}
			}
			
			Square casilleroPawnDerecha = Square.getSquare(pawnPasanteSquare.getFile() + 1, pawnPasanteSquare.getRank() - 1);
			if(casilleroPawnDerecha != null){
				origen = tablero.getPosicion(casilleroPawnDerecha);
				captura = tablero.getPosicion(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
				if (Piece.PAWN_WHITE.equals(origen.getValue())) {
			    	Move move = moveFactoryImp.createCaptureEnPassant(origen, tablero.getPosicion(pawnPasanteSquare), captura);
			    	moveContainer.add(move);
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
	
	private static Collection<Move> createContainer(){
		return new ArrayList<Move>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2237718042714336104L;

			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer(); 
				for (Move move : this) {
					buffer.append(move.toString() + "\n");
				}
				return buffer.toString();
			}
		};
	}	
}
