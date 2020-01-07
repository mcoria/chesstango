package movegenerators;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import chess.DummyBoard;
import chess.Move;
import chess.Square;

public class PeonBlancoMoveGenerator extends AbstractMoveGenerator{

	@Override
	public Set<Move> getPseudoMoves(DummyBoard dummyBoard, Square casillero) {
		Set<Move> moves = new HashSet<Move>();
		
		Square saltoSimpleCasillero = getCasilleroSaltoSimple(casillero);
		Square saltoDobleCasillero = getCasilleroSaltoDoble(casillero);
		
		Square casilleroAtaqueIzquirda = getCasilleroAtaqueIzquirda(casillero);
		Square casilleroAtaqueDerecha = getCasilleroAtaqueDerecha(casillero);
		
		if(saltoSimpleCasillero != null && dummyBoard.isEmtpy(saltoSimpleCasillero)){
			moves.add( new Move(casillero, saltoSimpleCasillero) );
		}
		
		if(saltoDobleCasillero != null && saltoDobleCasillero.getRank() == 3 && dummyBoard.isEmtpy(saltoDobleCasillero)){
			moves.add( new Move(casillero, saltoDobleCasillero) );
		}		
		
		return moves;
	}

	private Square getCasilleroSaltoSimple(Square casillero) {
		return Square.getSquare(casillero.getFile(), casillero.getRank() + 1);
	}

	private Square getCasilleroSaltoDoble(Square casillero) {
		return Square.getSquare(casillero.getFile(), casillero.getRank() + 2);
	}

	private Square getCasilleroAtaqueIzquirda(Square casillero) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Square getCasilleroAtaqueDerecha(Square casillero) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean puedeCapturarRey(DummyBoard dummyBoard, Square casillero, Square kingSquare) {
		// TODO Auto-generated method stub
		return false;
	}

}
