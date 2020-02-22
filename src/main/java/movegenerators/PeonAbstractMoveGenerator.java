package movegenerators;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Map;

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
	
	protected Collection<Move> getBasePseudoMoves(Map.Entry<Square, Pieza> origen) {
		Collection<Move> moves = createMoveContainer();
		
		Square casillero = origen.getKey();
		Square saltoSimpleCasillero = getCasilleroSaltoSimple(casillero);
		Square saltoDobleCasillero = getCasilleroSaltoDoble(casillero);
		
		Square casilleroAtaqueIzquirda = getCasilleroAtaqueIzquirda(casillero);
		Square casilleroAtaqueDerecha = getCasilleroAtaqueDerecha(casillero);
		
		Map.Entry<Square, Pieza> destino = null;
				
		if(saltoSimpleCasillero != null && this.tablero.isEmtpy(saltoSimpleCasillero)){
			destino = new SimpleImmutableEntry<Square, Pieza>(saltoSimpleCasillero, null);
			this.filter.filterMove(moves, new SimpleMove(origen, destino));
			
			if(saltoDobleCasillero != null && this.tablero.isEmtpy(saltoDobleCasillero)){
				destino = new SimpleImmutableEntry<Square, Pieza>(saltoDobleCasillero, null);
				this.filter.filterMove(moves, new SaltoDoblePeonMove(origen, destino));
			}			
		}
		
		if (casilleroAtaqueIzquirda != null) {
			Pieza pieza = this.tablero.getPieza(casilleroAtaqueIzquirda);
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				destino = new SimpleImmutableEntry<Square, Pieza>(casilleroAtaqueIzquirda, pieza);
				this.filter.filterMove(moves, new CaptureMove(origen, destino));
			}
		}	
		
		if (casilleroAtaqueDerecha != null) {
			Pieza pieza = this.tablero.getPieza(casilleroAtaqueDerecha);
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				destino = new SimpleImmutableEntry<Square, Pieza>(casilleroAtaqueDerecha, pieza);				
				this.filter.filterMove(moves, new CaptureMove(origen, destino));
			}
		}	
		
		return moves;
	}
	
	@Override
	public Collection<Move> generateMoves(Map.Entry<Square, Pieza> origen){
		BoardState boardState = this.tablero.getBoardState();
		Collection<Move> moves = getBasePseudoMoves(origen);
		
		if (boardState.getPeonPasanteSquare() != null) {
			Square casillero = origen.getKey();

			Map.Entry<Square, Pieza> destino = null;

			Square casilleroAtaqueIzquirda = getCasilleroAtaqueIzquirda(casillero);
			if(boardState.getPeonPasanteSquare().equals(casilleroAtaqueIzquirda)){
				destino = new SimpleImmutableEntry<Square, Pieza>(casilleroAtaqueIzquirda, null);
				this.filter.filterMove(moves, new CapturePeonPasante(origen, destino));
			}

			Square casilleroAtaqueDerecha = getCasilleroAtaqueDerecha(casillero);
			if(boardState.getPeonPasanteSquare().equals(casilleroAtaqueDerecha)){
				destino = new SimpleImmutableEntry<Square, Pieza>(casilleroAtaqueDerecha, null);
				this.filter.filterMove(moves, new CapturePeonPasante(origen, destino));		
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

}
