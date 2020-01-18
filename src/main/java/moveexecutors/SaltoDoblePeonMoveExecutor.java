package moveexecutors;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Square;

public class SaltoDoblePeonMoveExecutor implements MoveExecutor {

	@Override
	public void execute(DummyBoard board, BoardState boardState, Move move) {
		Square peonPasanteSquare = Square.getSquare(move.getTo().getKey().getFile(),  Color.BLANCO.equals(move.getFrom().getValue().getColor()) ? move.getTo().getKey().getRank() - 1 : move.getTo().getKey().getRank() + 1);
		board.setEmptySquare(move.getFrom().getKey());						//Dejamos origen
		board.setPieza(move.getTo().getKey(), move.getFrom().getValue());	//Vamos a destino
		
		
		boardState.setCaptura(null);
		boardState.setPeonPasanteSquare(peonPasanteSquare);
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState, Move move) {
		board.setEmptySquare(move.getTo().getKey());						//Reestablecemos destino
		board.setPieza(move.getFrom());										//Volvemos a origen
	}

}
