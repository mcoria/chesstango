package movegenerators;

import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

public abstract class AbstractMoveGenerator implements MoveGenerator {

	@Override
	public Set<Move> getLegalMoves(Board board, Square currentSquare) {
		DummyBoard tablero = board.getTablero();
		Set<Move> moves = new HashSet<Move>();
		Pieza currentPieza = tablero.getPieza(currentSquare);
		Set<Move> pseudoMoves = getPseudoMoves(tablero, currentSquare);
		for (Move move : pseudoMoves) {
			DummyBoard tableroNew = new DummyBoard(tablero);
			move.execute(tableroNew);
			if(! tableroNew.isKingInCheck(currentPieza.getColor()) ) {
				moves.add(move);
			}
		}
		return moves;
	}

}
