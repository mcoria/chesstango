/**
 * 
 */
package chess.pseudomovesgenerators;

import java.util.ArrayList;
import java.util.Collection;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.moves.Move;
import chess.moves.imp.MoveFactoryWhite;
import chess.position.PiecePlacement;
import chess.position.PositionState;

/**
 * @author Mauricio Coria
 *
 */
public class PawnPasanteMoveGenerator {
	
	private PositionState positionState;
	
	private PiecePlacement tablero;
	
	//TODO: this is WRONG
	protected MoveFactoryWhite moveFactoryImp = new MoveFactoryWhite();


	public Collection<Move> generatePseudoMoves() {
		Collection<Move> moveContainer = createContainer();
		
		PiecePositioned origen = null;
		PiecePositioned captura = null;
		
		Square peonPasanteSquare = positionState.getPawnPasanteSquare();

		if (peonPasanteSquare != null) {
			if(Color.WHITE.equals(positionState.getTurnoActual())){
				Square casilleroPawnIzquirda = Square.getSquare(peonPasanteSquare.getFile() - 1, peonPasanteSquare.getRank() - 1);
				if(casilleroPawnIzquirda != null){
					origen = this.tablero.getPosicion(casilleroPawnIzquirda);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() - 1));
					if (Piece.PAWN_WHITE.equals(origen.getValue())) {
				    	Move move = this.moveFactoryImp.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}
				
				Square casilleroPawnDerecha = Square.getSquare(peonPasanteSquare.getFile() + 1, peonPasanteSquare.getRank() - 1);
				if(casilleroPawnDerecha != null){
					origen = this.tablero.getPosicion(casilleroPawnDerecha);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() - 1));
					if (Piece.PAWN_WHITE.equals(origen.getValue())) {
				    	Move move = this.moveFactoryImp.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}				
			} else {
				Square casilleroPawnIzquirda = Square.getSquare(peonPasanteSquare.getFile() - 1, peonPasanteSquare.getRank() + 1);
				if(casilleroPawnIzquirda != null){
					origen = this.tablero.getPosicion(casilleroPawnIzquirda);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() + 1));
					if (Piece.PAWN_BLACK.equals(origen.getValue())) {
				    	Move move = this.moveFactoryImp.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}
				
				Square casilleroPawnDerecha = Square.getSquare(peonPasanteSquare.getFile() + 1, peonPasanteSquare.getRank() + 1);
				if(casilleroPawnDerecha != null){
					origen = this.tablero.getPosicion(casilleroPawnDerecha);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() + 1));
					if (Piece.PAWN_BLACK.equals(origen.getValue())) {
				    	Move move = this.moveFactoryImp.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}				
			}
		}
		
		return moveContainer;
	}


	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
	}


	public void setTablero(PiecePlacement tablero) {
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
