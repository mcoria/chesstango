package moveexecutors;

import java.util.Map;
import java.util.Map.Entry;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class CaptureMove extends AbstractMove {
	
	public CaptureMove(Entry<Square, Pieza> from, Entry<Square, Pieza> to) {
		super(from, to);
	}

	@Override
	public void execute(DummyBoard board) {
		this.execute(board, from, to);
		BoardState boardState = board.getBoardState();
		boardState.setPeonPasanteSquare(null);
		boardState.rollTurno();		
	}

	@Override
	public void undo(DummyBoard board) {
		this.undo(board, from, to);
		BoardState boardState = board.getBoardState();		
		boardState.restoreState();		
	}

	@Override
	protected String getType() {
		return "CaptureMove";
	}	
	
	public void execute(DummyBoard board, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {			
		board.setEmptySquare(from.getKey());								//Dejamos el origen
		board.setPieza(to.getKey(), from.getValue());						//Vamos al destino	
	}

	public void undo(DummyBoard board, Entry<Square, Pieza> from, Entry<Square, Pieza> to) {
		board.setPosicion(to);							//Reestablecemos destino
		board.setPosicion(from);						//Volvemos a origen
	}
}
