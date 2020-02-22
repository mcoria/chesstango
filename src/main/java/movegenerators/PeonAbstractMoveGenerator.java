package movegenerators;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
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
	public Collection<Move> generateMoves(Map.Entry<Square, Pieza> origen){
		BoardState boardState = this.tablero.getBoardState();
		Collection<Move> moves = createMoveContainer();
		
		Square casillero = origen.getKey();
		Square saltoSimpleCasillero = getCasilleroSaltoSimple(casillero);
		Square saltoDobleCasillero = getCasilleroSaltoDoble(casillero);
		
		Square casilleroAtaqueIzquirda = getCasilleroAtaqueIzquirda(casillero);
		Square casilleroAtaqueDerecha = getCasilleroAtaqueDerecha(casillero);
		
		Square peonPasanteSquare = boardState.getPeonPasanteSquare();
		
			
		Map.Entry<Square, Pieza> destino = null;
		
		if (saltoSimpleCasillero != null && this.tablero.isEmtpy(saltoSimpleCasillero)) {
			destino = this.tablero.getPosicion(saltoSimpleCasillero);
			if (destino.getValue() == null) {
				this.filter.filterMove(moves, new SimpleMove(origen, destino));
				if (saltoDobleCasillero != null) {
					destino = this.tablero.getPosicion(saltoDobleCasillero);
					if (destino.getValue() == null) {
						this.filter.filterMove(moves, new SaltoDoblePeonMove(origen, destino, saltoSimpleCasillero));
					}
				}
			}
		}
		
		if (casilleroAtaqueIzquirda != null) {
			destino = this.tablero.getPosicion(casilleroAtaqueIzquirda);
			Pieza pieza = destino.getValue();
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				this.filter.filterMove(moves, new CaptureMove(origen, destino));
			}
		}	
		
		if (casilleroAtaqueDerecha != null) {
			destino = this.tablero.getPosicion(casilleroAtaqueDerecha);
			Pieza pieza = destino.getValue();
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {				
				this.filter.filterMove(moves, new CaptureMove(origen, destino));
			}
		}	
		
		if (peonPasanteSquare != null) {
			if (peonPasanteSquare.equals(casilleroAtaqueIzquirda) || peonPasanteSquare.equals(casilleroAtaqueDerecha)) {
				destino = this.tablero.getPosicion(peonPasanteSquare);
				this.filter.filterMove(moves, new CapturePeonPasante(origen, destino, getCapturaPeonPasante(peonPasanteSquare)));
			}
		}
		
		return moves;
	}

	@Override
	public boolean puedeCapturarRey(DummyBoard dummyBoard, Map.Entry<Square, Pieza> origen, Square kingSquare) {
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
	
	protected abstract Entry<Square, Pieza> getCapturaPeonPasante(Square peonPasanteSquare);

}
