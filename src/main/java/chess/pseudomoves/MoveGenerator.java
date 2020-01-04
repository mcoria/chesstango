package chess.pseudomoves;

import java.util.Set;

import chess.Move;
import chess.Pieza;
import chess.Square;

public interface MoveGenerator {
	public Set<Move> getMoves(Pieza[] tablero, Square casillero);
}
