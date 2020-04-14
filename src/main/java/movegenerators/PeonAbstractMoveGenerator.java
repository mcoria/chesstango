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
	
	public PeonAbstractMoveGenerator(Color color) {
		this.color = color;
	}
	
	@Override
	public void generateMoves(PosicionPieza origen, Collection<Move> moveContainer){
		BoardState boardState = this.tablero.getBoardState();
		
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
				this.filter.filterMove(moveContainer, new SimpleMove(origen, destino));
				if (saltoDobleCasillero != null) {
					destino = this.tablero.getPosicion(saltoDobleCasillero);
					if (destino.getValue() == null) {
						this.filter.filterMove(moveContainer, new SaltoDoblePeonMove(origen, destino, saltoSimpleCasillero));
					}
				}
			}
		}
		
		if (casilleroAtaqueIzquirda != null) {
			destino = this.tablero.getPosicion(casilleroAtaqueIzquirda);
			Pieza pieza = destino.getValue();
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				this.filter.filterMove(moveContainer, new CaptureMove(origen, destino));
			}
		}	
		
		if (casilleroAtaqueDerecha != null) {
			destino = this.tablero.getPosicion(casilleroAtaqueDerecha);
			Pieza pieza = destino.getValue();
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {				
				this.filter.filterMove(moveContainer, new CaptureMove(origen, destino));
			}
		}	
		
		if (peonPasanteSquare != null) {
			if (peonPasanteSquare.equals(casilleroAtaqueIzquirda) || peonPasanteSquare.equals(casilleroAtaqueDerecha)) {
				destino = this.tablero.getPosicion(peonPasanteSquare);
				this.filter.filterMove(moveContainer, new CapturePeonPasante(origen, destino, getCapturaPeonPasante(peonPasanteSquare)));
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

	protected abstract Square getCasilleroSaltoSimple(Square casillero);

	protected abstract Square getCasilleroSaltoDoble(Square casillero);

	protected abstract Square getCasilleroAtaqueIzquirda(Square casillero);
	
	protected abstract Square getCasilleroAtaqueDerecha(Square casillero);
	
	protected abstract PosicionPieza getCapturaPeonPasante(Square peonPasanteSquare);

}
