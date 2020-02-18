package moveexecutors;

import java.util.Map;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class CapturePeonPasanteExecutor implements MoveExecutor {

	@Override
	public void execute(DummyBoard board, BoardState boardState, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {
		Square captureSquare = Square.getSquare(to.getKey().getFile(),  Color.BLANCO.equals(from.getValue().getColor()) ? to.getKey().getRank() - 1 : to.getKey().getRank() + 1);		
		Map.Entry<Square, Pieza> captura = board.getPosicion(captureSquare);
				
		board.setEmptySquare(from.getKey()); 						//Dejamos el origen
		board.setPieza(to.getKey(), from.getValue());				//Vamos al destino
		board.setEmptySquare(captureSquare);						//Capturamos peon
		
		boardState.setFrom(from);
		boardState.setTo(to);
		boardState.setCaptura(captura);
		boardState.setPeonPasanteSquare(null);	
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState) {
		board.setPosicion(boardState.getCaptura());					//Devolvemos peon
		board.setPosicion(boardState.getTo());						//Reestablecemos destino
		board.setPosicion(boardState.getFrom());					//Volvemos a origen
	}

}
