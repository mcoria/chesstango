package moveexecutors;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

public class CapturePeonPasanteExecutor implements MoveExecutor {

	@Override
	public void execute(DummyBoard board, BoardState boardState, Move move) {
		Square captureSquare = Square.getSquare(move.getTo().getKey().getFile(),  Color.BLANCO.equals(move.getFrom().getValue().getColor()) ? move.getTo().getKey().getRank() - 1 : move.getTo().getKey().getRank() + 1);
		Pieza peonCapturado = Color.BLANCO.equals(move.getFrom().getValue().getColor()) ? Pieza.PEON_NEGRO : Pieza.PEON_BLANCO;
		Map.Entry<Square, Pieza> captura = new SimpleImmutableEntry<Square, Pieza>(captureSquare, peonCapturado);
		
		
		board.setEmptySquare(move.getFrom().getKey()); 						//Dejamos el origen
		board.setPieza(move.getTo().getKey(), move.getFrom().getValue());	//Vamos al destino
		board.setEmptySquare(captureSquare);								//Capturamos peon
		
		boardState.setCaptura(captura);
		boardState.setPeonPasanteSquare(null);
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState, Move move) {
		board.setPieza(boardState.getCaptura());							//Devolvemos peon
		board.setEmptySquare(move.getTo().getKey());						//Reestablecemos destino
		board.setPieza(move.getFrom());										//Volvemos a origen
	}
	
	/*
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CapturePeonPasanteExecutor){
			CapturePeonPasanteExecutor theOther = (CapturePeonPasanteExecutor) obj;
			return Objects.equals(casilleroPeonPasante, theOther.casilleroPeonPasante);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return casilleroPeonPasante.hashCode();
	}
	
	@Override
	public String toString() {
		return "Peon Pasante: " + casilleroPeonPasante.toString();
	}	
	*/

}
