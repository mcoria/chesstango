package movegenerators;

import java.util.Collection;

import chess.BoardState;
import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.CaptureMove;
import moveexecutors.CapturePeonPasante;
import moveexecutors.SaltoDoblePeonMove;
import moveexecutors.SimpleMove;

public abstract class PeonAbstractMoveGenerator extends AbstractMoveGenerator {

	protected Color color;
	
	protected BoardState boardState;

	protected abstract Square getCasilleroSaltoSimple(Square casillero);

	protected abstract Square getCasilleroSaltoDoble(Square casillero);

	protected abstract Square getCasilleroAtaqueIzquirda(Square casillero);
	
	protected abstract Square getCasilleroAtaqueDerecha(Square casillero);
	
	protected abstract PosicionPieza getCapturaPeonPasante(Square peonPasanteSquare);	
	
	public PeonAbstractMoveGenerator(Color color) {
		this.color = color;
	}
	
	@Override
	public void generateMoves(PosicionPieza origen, Collection<Move> moveContainer){
		
		Square casillero = origen.getKey();
		Square saltoSimpleCasillero = getCasilleroSaltoSimple(casillero);
		Square saltoDobleCasillero = getCasilleroSaltoDoble(casillero);
		
		Square casilleroAtaqueIzquirda = getCasilleroAtaqueIzquirda(casillero);
		Square casilleroAtaqueDerecha = getCasilleroAtaqueDerecha(casillero);
		
		Square peonPasanteSquare = boardState.getPeonPasanteSquare();
		
			
		PosicionPieza destino = null;
		
		if (saltoSimpleCasillero != null && this.tablero.isEmtpy(saltoSimpleCasillero)) {
			destino = this.tablero.getPosicion(saltoSimpleCasillero);
			if (destino.getValue() == null) {
		    	Move moveSaltoSimple = new SimpleMove(origen, destino);
				if(this.filter.filterMove(moveSaltoSimple)){
					moveContainer.add(moveSaltoSimple);
				}
				if (saltoDobleCasillero != null) {
					destino = this.tablero.getPosicion(saltoDobleCasillero);
					if (destino.getValue() == null) {
				    	Move moveSaltoDoble = new SaltoDoblePeonMove(origen, destino, saltoSimpleCasillero);
						if(this.filter.filterMove(moveSaltoDoble)){
							moveContainer.add(moveSaltoDoble);
						}
					}
				}
			}
		}
		
		if (casilleroAtaqueIzquirda != null) {
			destino = this.tablero.getPosicion(casilleroAtaqueIzquirda);
			Pieza pieza = destino.getValue();
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
		    	Move move = new CaptureMove(origen, destino);
				if(this.filter.filterMove(move)){
					moveContainer.add(move);
				}				
			}
		}	
		
		if (casilleroAtaqueDerecha != null) {
			destino = this.tablero.getPosicion(casilleroAtaqueDerecha);
			Pieza pieza = destino.getValue();
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {				
		    	Move move = new CaptureMove(origen, destino);
				if(this.filter.filterMove(move)){
					moveContainer.add(move);
				}				
			}
		}	
		
		if (peonPasanteSquare != null) {
			if (peonPasanteSquare.equals(casilleroAtaqueIzquirda) || peonPasanteSquare.equals(casilleroAtaqueDerecha)) {
				destino = this.tablero.getPosicion(peonPasanteSquare);
		    	Move move = new CapturePeonPasante(origen, destino, getCapturaPeonPasante(peonPasanteSquare));
				if(this.filter.filterMove(move)){
					moveContainer.add(move);
				}				
			}
		}
	}

	@Override
	public boolean puedeCapturarRey(PosicionPieza origen, Square kingSquare) {
		if(kingSquare.equals(getCasilleroAtaqueIzquirda(origen.getKey())) ||
		   kingSquare.equals(getCasilleroAtaqueDerecha(origen.getKey())) ){
			return true;
		}
		return false;
	}


	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}

}
