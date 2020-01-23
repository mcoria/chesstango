package moveexecutors;

import java.util.AbstractMap.SimpleImmutableEntry;
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
		Pieza peonCapturado = Color.BLANCO.equals(from.getValue().getColor()) ? Pieza.PEON_NEGRO : Pieza.PEON_BLANCO;		
		Map.Entry<Square, Pieza> captura = new SimpleImmutableEntry<Square, Pieza>(captureSquare, peonCapturado);
		
		boardState.setFrom(from);
		boardState.setTo(to);		
		board.setEmptySquare(from.getKey()); 						//Dejamos el origen
		board.setPieza(to.getKey(), from.getValue());				//Vamos al destino
		board.setEmptySquare(captureSquare);						//Capturamos peon
		
		boardState.setCaptura(captura);
		boardState.setPeonPasanteSquare(null);	
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState) {
		board.setPieza(boardState.getCaptura());					//Devolvemos peon
		board.setEmptySquare(boardState.getTo().getKey());			//Reestablecemos destino
		board.setPieza(boardState.getFrom());						//Volvemos a origen
	}

}
