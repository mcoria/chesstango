package movegenerators;

import java.util.Map;
import java.util.Set;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import moveexecutors.CaptureMoveExecutor;
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
		
		if(saltoSimpleCasillero != null && dummyBoard.isEmtpy(saltoSimpleCasillero)){
			moves.add( new Move(casillero, saltoSimpleCasillero, new SimpleMoveExecutor(peon)) );
			
			if(saltoDobleCasillero != null && dummyBoard.isEmtpy(saltoDobleCasillero)){
				moves.add( new Move(casillero, saltoDobleCasillero, new SimpleMoveExecutor(peon)) );
			}			
		}
		
		if (casilleroAtaqueIzquirda != null) {
			Pieza pieza = dummyBoard.getPieza(casilleroAtaqueIzquirda);
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				moves.add(new Move(casillero, casilleroAtaqueIzquirda, new CaptureMoveExecutor(peon, pieza)));
			}
		}	
		
		if (casilleroAtaqueDerecha != null) {
			Pieza pieza = dummyBoard.getPieza(casilleroAtaqueDerecha);
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				moves.add(new Move(casillero, casilleroAtaqueDerecha, new CaptureMoveExecutor(peon, pieza)));
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

}
