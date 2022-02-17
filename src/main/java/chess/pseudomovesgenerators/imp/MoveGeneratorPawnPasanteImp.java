/**
 * 
 */
package chess.pseudomovesgenerators.imp;

import java.util.ArrayList;
import java.util.Collection;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.moves.Move;
import chess.moves.imp.MoveFactoryBlack;
import chess.moves.imp.MoveFactoryWhite;
import chess.position.PiecePlacementReader;
import chess.position.imp.PositionState;
import chess.pseudomovesgenerators.MoveGeneratorPawnPasante;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorPawnPasanteImp implements MoveGeneratorPawnPasante {
	
	private PawnPasanteMoveGeneratorBlack pasanteMoveGeneratorBlack = new PawnPasanteMoveGeneratorBlack();
	private PawnPasanteMoveGeneratorWhite pasanteMoveGeneratorWhite = new PawnPasanteMoveGeneratorWhite();
	
	private PositionState positionState;
	
	private PiecePlacementReader tablero;
	

	@Override
	public Collection<Move> generatoPawnPasantePseudoMoves() {
		Collection<Move> moveContainer = createContainer();
		Square peonPasanteSquare = positionState.getPawnPasanteSquare();
		if (peonPasanteSquare != null) {
			if (Color.WHITE.equals(positionState.getTurnoActual())) {
				return pasanteMoveGeneratorWhite.generatePseudoMoves(moveContainer, peonPasanteSquare);
			} else {
				return pasanteMoveGeneratorBlack.generatePseudoMoves(moveContainer, peonPasanteSquare);
			}
		}
		return moveContainer;
	}


	private class PawnPasanteMoveGeneratorBlack{
		private MoveFactoryBlack moveFactoryImp = new MoveFactoryBlack();
		
		public Collection<Move> generatePseudoMoves(Collection<Move> moveContainer, Square peonPasanteSquare) {
			PiecePositioned origen = null;
			PiecePositioned captura = null;

			Square casilleroPawnIzquirda = Square.getSquare(peonPasanteSquare.getFile() - 1, peonPasanteSquare.getRank() + 1);
			if(casilleroPawnIzquirda != null){
				origen = tablero.getPosicion(casilleroPawnIzquirda);
				captura = tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() + 1));
				if (Piece.PAWN_BLACK.equals(origen.getValue())) {
			    	Move move = moveFactoryImp.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
			    	moveContainer.add(move);
				}
			}
			
			Square casilleroPawnDerecha = Square.getSquare(peonPasanteSquare.getFile() + 1, peonPasanteSquare.getRank() + 1);
			if(casilleroPawnDerecha != null){
				origen = tablero.getPosicion(casilleroPawnDerecha);
				captura = tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() + 1));
				if (Piece.PAWN_BLACK.equals(origen.getValue())) {
			    	Move move = moveFactoryImp.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
			    	moveContainer.add(move);
				}
			}				
			
			return moveContainer;
		}
	}
	
	private class PawnPasanteMoveGeneratorWhite{
		private MoveFactoryWhite moveFactoryImp = new MoveFactoryWhite();		

		public Collection<Move> generatePseudoMoves(Collection<Move> moveContainer, Square peonPasanteSquare) {
			PiecePositioned origen = null;
			PiecePositioned captura = null;

			Square casilleroPawnIzquirda = Square.getSquare(peonPasanteSquare.getFile() - 1, peonPasanteSquare.getRank() - 1);
			if(casilleroPawnIzquirda != null){
				origen = tablero.getPosicion(casilleroPawnIzquirda);
				captura = tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() - 1));
				if (Piece.PAWN_WHITE.equals(origen.getValue())) {
			    	Move move = this.moveFactoryImp.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
			    	moveContainer.add(move);
				}
			}
			
			Square casilleroPawnDerecha = Square.getSquare(peonPasanteSquare.getFile() + 1, peonPasanteSquare.getRank() - 1);
			if(casilleroPawnDerecha != null){
				origen = tablero.getPosicion(casilleroPawnDerecha);
				captura = tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() - 1));
				if (Piece.PAWN_WHITE.equals(origen.getValue())) {
			    	Move move = moveFactoryImp.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
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
