package movegenerators;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Map;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Move.MoveType;
import chess.Pieza;
import chess.Square;

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
			this.filter.filterMove(moves, new Move(origen, destino, MoveType.SIMPLE));
			
			if(saltoDobleCasillero != null && this.tablero.isEmtpy(saltoDobleCasillero)){
				destino = new SimpleImmutableEntry<Square, Pieza>(saltoDobleCasillero, null);
				this.filter.filterMove(moves, new Move(origen, destino, MoveType.SALTO_DOBLE_PEON));
			}			
		}
		
		if (casilleroAtaqueIzquirda != null) {
			Pieza pieza = this.tablero.getPieza(casilleroAtaqueIzquirda);
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				destino = new SimpleImmutableEntry<Square, Pieza>(casilleroAtaqueIzquirda, pieza);
				this.filter.filterMove(moves, new Move(origen, destino, MoveType.CAPTURA));
			}
		}	
		
		if (casilleroAtaqueDerecha != null) {
			Pieza pieza = this.tablero.getPieza(casilleroAtaqueDerecha);
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				destino = new SimpleImmutableEntry<Square, Pieza>(casilleroAtaqueDerecha, pieza);				
				this.filter.filterMove(moves, new Move(origen, destino, MoveType.CAPTURA));
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
				this.filter.filterMove(moves, new Move(origen, destino, MoveType.CAPTURA_PEON_PASANTE));
			}

			Square casilleroAtaqueDerecha = getCasilleroAtaqueDerecha(casillero);
			if(boardState.getPeonPasanteSquare().equals(casilleroAtaqueDerecha)){
				destino = new SimpleImmutableEntry<Square, Pieza>(casilleroAtaqueDerecha, null);
				this.filter.filterMove(moves, new Move(origen, destino, MoveType.CAPTURA_PEON_PASANTE));		
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
