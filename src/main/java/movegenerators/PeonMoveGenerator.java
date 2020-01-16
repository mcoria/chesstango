package movegenerators;

import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import moveexecutors.CaptureMoveExecutor;
import moveexecutors.CapturePeonPasanteExecutor;
import moveexecutors.SimpleMoveExecutor;

public class PeonMoveGenerator extends AbstractMoveGenerator {

	private Color color;
	
	public PeonMoveGenerator(Color color) {
		this.color = color;
	}
	
	@Override
	public Set<Move> getPseudoMoves(DummyBoard dummyBoard, Map.Entry<Square, Pieza> origen) {
		Square casillero = origen.getKey();
		Pieza peon = origen.getValue();
		Set<Move> moves = createMoveContainer();
		
		Square saltoSimpleCasillero = getCasilleroSaltoSimple(casillero);
		Square saltoDobleCasillero = getCasilleroSaltoDoble(casillero);
		
		Square casilleroAtaqueIzquirda = getCasilleroAtaqueIzquirda(casillero);
		Square casilleroAtaqueDerecha = getCasilleroAtaqueDerecha(casillero);
		
		Square casilleroIzquierda = getCasilleroIzquirda(casillero);
		Square casilleroDerecha = getCasilleroDerecha(casillero);
		
		Map.Entry<Square, Pieza> destino = null;
				
		if(saltoSimpleCasillero != null && dummyBoard.isEmtpy(saltoSimpleCasillero)){
			destino = new SimpleImmutableEntry<Square, Pieza>(saltoSimpleCasillero, null);
			moves.add( new Move(origen, destino, new SimpleMoveExecutor(peon)) );
			
			if(saltoDobleCasillero != null && dummyBoard.isEmtpy(saltoDobleCasillero)){
				destino = new SimpleImmutableEntry<Square, Pieza>(saltoDobleCasillero, null);
				moves.add( new Move(origen, destino, new SimpleMoveExecutor(peon)) );
			}			
		}
		
		if (casilleroAtaqueIzquirda != null) {
			Pieza pieza = dummyBoard.getPieza(casilleroAtaqueIzquirda);
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				destino = new SimpleImmutableEntry<Square, Pieza>(casilleroAtaqueIzquirda, pieza);
				moves.add(new Move(origen, destino, new CaptureMoveExecutor(peon, pieza)));
			}
		}	
		
		if (casilleroAtaqueDerecha != null) {
			Pieza pieza = dummyBoard.getPieza(casilleroAtaqueDerecha);
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				destino = new SimpleImmutableEntry<Square, Pieza>(casilleroAtaqueIzquirda, pieza);
				moves.add(new Move(origen, destino, new CaptureMoveExecutor(peon, pieza)));
			}
		}
		
		if(casilleroIzquierda != null) {
			Pieza pieza = dummyBoard.getPieza(casilleroIzquierda);
			if (pieza != null && color.opositeColor().equals(pieza.getColor()) && pieza.isPeon()) {
				destino = new SimpleImmutableEntry<Square, Pieza>(casilleroAtaqueIzquirda, null);
				moves.add(new Move(origen, destino, new CapturePeonPasanteExecutor(casilleroIzquierda)));
			}			
		}
		
		if(casilleroDerecha != null) {
			Pieza pieza = dummyBoard.getPieza(casilleroDerecha);
			if (pieza != null && color.opositeColor().equals(pieza.getColor()) && pieza.isPeon()) {
				destino = new SimpleImmutableEntry<Square, Pieza>(casilleroAtaqueDerecha, null);
				moves.add(new Move(origen, destino, new CapturePeonPasanteExecutor(casilleroDerecha)));
			}			
		}		
		
		return moves;
	}

	private Square getCasilleroSaltoSimple(Square casillero) {
		Square value = null;
		switch (color) {
		case BLANCO:
			value = Square.getSquare(casillero.getFile(), casillero.getRank() + 1);
			break;
		case NEGRO:
			value = Square.getSquare(casillero.getFile(), casillero.getRank() - 1);
			break;
		}
		return value;
	}

	private Square getCasilleroSaltoDoble(Square casillero) {
		Square value = null;
		switch (color) {
		case BLANCO:
			value = casillero.getRank() == 1 ? Square.getSquare(casillero.getFile(), 3) : null;
			break;
		case NEGRO:
			value = casillero.getRank() == 6 ? Square.getSquare(casillero.getFile(), 4) : null;
			break;
		}
		return value;		
	}

	private Square getCasilleroAtaqueIzquirda(Square casillero) {
		Square value = null;
		switch (color) {
		case BLANCO:
			value = Square.getSquare(casillero.getFile() - 1, casillero.getRank() + 1);
			break;
		case NEGRO:
			value = Square.getSquare(casillero.getFile() - 1, casillero.getRank() - 1);
			break;
		}
		return value;			
	}
	
	private Square getCasilleroAtaqueDerecha(Square casillero) {
		Square value = null;
		switch (color) {
		case BLANCO:
			value = Square.getSquare(casillero.getFile() + 1, casillero.getRank() + 1);
			break;
		case NEGRO:
			value = Square.getSquare(casillero.getFile() + 1, casillero.getRank() - 1);
			break;
		}
		return value;		
	}
	

	private Square getCasilleroIzquirda(Square casillero) {
		Square value = null;
		switch (color) {
		case BLANCO:
			value = casillero.getRank() == 4 ? Square.getSquare(casillero.getFile() - 1, 4) : null;
			break;
		case NEGRO:
			value = casillero.getRank() == 3 ? Square.getSquare(casillero.getFile() - 1, 3) : null;
			break;
		}
		return value;
	}
	
	private Square getCasilleroDerecha(Square casillero) {
		Square value = null;
		switch (color) {
		case BLANCO:
			value = casillero.getRank() == 4 ? Square.getSquare(casillero.getFile() + 1, 4) : null;
			break;
		case NEGRO:
			value = casillero.getRank() == 3 ? Square.getSquare(casillero.getFile() + 1, 3) : null;
			break;
		}
		return value;	
	}	

}
