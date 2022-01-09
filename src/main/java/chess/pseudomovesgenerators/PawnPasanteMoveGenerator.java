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
import chess.layers.ChessPositionState;
import chess.layers.PiecePlacement;
import chess.moves.Move;
import chess.moves.MoveFactory;

/**
 * @author Mauricio Coria
 *
 */
public class PawnPasanteMoveGenerator {
	
	private ChessPositionState chessPositionState;
	
	private PiecePlacement tablero;
	
	protected MoveFactory moveFactory = new MoveFactory();


	public Collection<Move> getPseudoMoves() {
		Collection<Move> moveContainer = createContainer();
		
		PiecePositioned origen = null;
		PiecePositioned captura = null;
		
		Square peonPasanteSquare = chessPositionState.getPawnPasanteSquare();

		if (peonPasanteSquare != null) {
			if(Color.WHITE.equals(chessPositionState.getTurnoActual())){
				Square casilleroPawnIzquirda = Square.getSquare(peonPasanteSquare.getFile() - 1, peonPasanteSquare.getRank() - 1);
				if(casilleroPawnIzquirda != null){
					origen = this.tablero.getPosicion(casilleroPawnIzquirda);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() - 1));
					if (Piece.PAWN_WHITE.equals(origen.getValue())) {
				    	Move move = this.moveFactory.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}
				
				Square casilleroPawnDerecha = Square.getSquare(peonPasanteSquare.getFile() + 1, peonPasanteSquare.getRank() - 1);
				if(casilleroPawnDerecha != null){
					origen = this.tablero.getPosicion(casilleroPawnDerecha);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() - 1));
					if (Piece.PAWN_WHITE.equals(origen.getValue())) {
				    	Move move = this.moveFactory.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}				
			} else {
				Square casilleroPawnIzquirda = Square.getSquare(peonPasanteSquare.getFile() - 1, peonPasanteSquare.getRank() + 1);
				if(casilleroPawnIzquirda != null){
					origen = this.tablero.getPosicion(casilleroPawnIzquirda);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() + 1));
					if (Piece.PAWN_BLACK.equals(origen.getValue())) {
				    	Move move = this.moveFactory.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}
				
				Square casilleroPawnDerecha = Square.getSquare(peonPasanteSquare.getFile() + 1, peonPasanteSquare.getRank() + 1);
				if(casilleroPawnDerecha != null){
					origen = this.tablero.getPosicion(casilleroPawnDerecha);
					captura = this.tablero.getPosicion(Square.getSquare(peonPasanteSquare.getFile(), peonPasanteSquare.getRank() + 1));
					if (Piece.PAWN_BLACK.equals(origen.getValue())) {
				    	Move move = this.moveFactory.createCapturePawnPasante(origen, tablero.getPosicion(peonPasanteSquare), captura);
				    	moveContainer.add(move);
					}
				}				
			}
		}
		
		return moveContainer;
	}


	public void setBoardState(ChessPositionState chessPositionState) {
		this.chessPositionState = chessPositionState;
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
