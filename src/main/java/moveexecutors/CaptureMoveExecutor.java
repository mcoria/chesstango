package moveexecutors;

import chess.BoardMediator;
import chess.Move;
import chess.Pieza;

public class CaptureMoveExecutor implements MoveExecutor {

	@Override
	public void execute(BoardMediator board, Move move) {
		board.setPieza(move.getTo(), board.getPieza(move.getFrom()));
		board.setEmptySquare(move.getFrom());
	}

	@Override
	public void undo(BoardMediator board, Move move) {
		Pieza movedPieza = board.getPieza(move.getTo());
		Pieza capturada = move.getCapturada();
		board.setPieza(move.getFrom(), movedPieza);
		board.setPieza(move.getTo(), capturada);
	}

}
