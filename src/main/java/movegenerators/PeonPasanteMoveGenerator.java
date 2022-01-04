/**
 * 
 */
package movegenerators;

import java.util.ArrayList;
import java.util.Collection;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import layers.PosicionPiezaBoard;
import moveexecutors.Move;
import moveexecutors.MoveFactory;

/**
 * @author Mauricio Coria
 *
 */
public class PeonPasanteMoveGenerator {
	
	private BoardState boardState;
	
	private PosicionPiezaBoard tablero;
	
	protected MoveFactory moveFactory = new MoveFactory();


	public Collection<Move> getPseudoMoves() {
		Collection<Move> moveContainer = createContainer();
		
		PosicionPieza origen = null;
		PosicionPieza captura = null;
		
		Square peonPasanteSquare = boardState.getPeonPasanteSquare();

		if (peonPasanteSquare != null) {
			if(Color.BLANCO.equals(boardState.getTurnoActual())){
				Square casilleroPeonIzquirda = Square.getSquare(peonPasanteSquare.getFile() - 1, peonPasanteSquare.getRank() - 1);
				if(casilleroPeonIzquirda != null){
					origen = this.tablero.getPosicion(casilleroPeonIzquirda);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() - 1));
					if (Pieza.PEON_BLANCO.equals(origen.getValue())) {
				    	Move move = this.moveFactory.createCapturePeonPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}
				
				Square casilleroPeonDerecha = Square.getSquare(peonPasanteSquare.getFile() + 1, peonPasanteSquare.getRank() - 1);
				if(casilleroPeonDerecha != null){
					origen = this.tablero.getPosicion(casilleroPeonDerecha);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() - 1));
					if (Pieza.PEON_BLANCO.equals(origen.getValue())) {
				    	Move move = this.moveFactory.createCapturePeonPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}				
			} else {
				Square casilleroPeonIzquirda = Square.getSquare(peonPasanteSquare.getFile() - 1, peonPasanteSquare.getRank() + 1);
				if(casilleroPeonIzquirda != null){
					origen = this.tablero.getPosicion(casilleroPeonIzquirda);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() + 1));
					if (Pieza.PEON_NEGRO.equals(origen.getValue())) {
				    	Move move = this.moveFactory.createCapturePeonPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}
				
				Square casilleroPeonDerecha = Square.getSquare(peonPasanteSquare.getFile() + 1, peonPasanteSquare.getRank() + 1);
				if(casilleroPeonDerecha != null){
					origen = this.tablero.getPosicion(casilleroPeonDerecha);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() + 1));
					if (Pieza.PEON_NEGRO.equals(origen.getValue())) {
				    	Move move = this.moveFactory.createCapturePeonPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}				
			}
		}
		
		return moveContainer;
	}


	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}


	public void setTablero(PosicionPiezaBoard tablero) {
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
