package movegenerators;

import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

//Template  Method Pattern GoF
public abstract class AbstractMoveGenerator implements MoveGenerator {

	@Override
	public Set<Move> getLegalMoves(Board board, Square currentSquare) {
		DummyBoard tablero = board.getTablero();
		Set<Move> moves = new HashSet<Move>();
		Pieza currentPieza = tablero.getPieza(currentSquare);
		Set<Move> pseudoMoves = getPseudoMoves(tablero, currentSquare);
		for (Move move : pseudoMoves) {
			move.execute(tablero.getMediator());
			if(! tablero.isKingInCheck(currentPieza.getColor()) ) {
				moves.add(move);
			}
			move.undo(tablero.getMediator());
		}
		return moves;
	}
	
	@Override
	public boolean puedeCapturarRey(DummyBoard dummyBoard, Square casillero, Square kingSquare) {
		Set<Move> pseudoMoves = getPseudoMoves(dummyBoard, casillero);
		for (Move move : pseudoMoves) {
			if(kingSquare.equals(move.getTo())){
				return true;
			}
		}
		return false;
	}

}
