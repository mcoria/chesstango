package movegenerators;

import java.util.HashSet;
import java.util.Set;

import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

public abstract class AbstractMoveGenerator implements MoveGenerator{

	@Override
	public Set<Move> getLegalMoves(DummyBoard tablero, Square currentSquare) {
		Set<Move> moves = new HashSet<Move>();
		Pieza currentPieza = tablero.getPieza(currentSquare);
		Set<Move> pseudoMoves = getPseudoMoves(tablero, currentSquare);
		for (Move move : pseudoMoves) {
			DummyBoard tableroNew = new DummyBoard(tablero);
			tableroNew.move(move);
			if(! tableroNew.isKingInCheck(currentPieza.getColor()) ) {
				moves.add(move);
			}
		}
		return moves;
	}

}
