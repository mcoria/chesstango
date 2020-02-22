package moveexecutors;

import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class CapturePeonPasante extends AbstractMove {

	public CapturePeonPasante(Entry<Square, Pieza> from, Entry<Square, Pieza> to) {
		super(from, to);
	}

	@Override
	public void execute(DummyBoard board) {
		this.executeMove(board);
		
		BoardState boardState = board.getBoardState();
		boardState.setPeonPasanteSquare(null);	
		boardState.rollTurno();		
	}

	@Override
	public void undo(DummyBoard board) {
		this.undoMove(board);
		
		BoardState boardState = board.getBoardState();		
		boardState.restoreState();			
	}
	
	@Override
	protected String getType() {
		return "CapturePeonPasante";
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		Square captureSquare = Square.getSquare(to.getKey().getFile(),  Color.BLANCO.equals(from.getValue().getColor()) ? to.getKey().getRank() - 1 : to.getKey().getRank() + 1);
				
		board.setEmptySquare(from.getKey()); 						//Dejamos el origen
		board.setPieza(to.getKey(), from.getValue());				//Vamos al destino
		board.setEmptySquare(captureSquare);						//Capturamos peon
	}

	@Override
	public void undoMove(DummyBoard board) {
		Square captureSquare = Square.getSquare(to.getKey().getFile(),  Color.BLANCO.equals(from.getValue().getColor()) ? to.getKey().getRank() - 1 : to.getKey().getRank() + 1);
		Map.Entry<Square, Pieza> captura = new SimpleImmutableEntry<Square, Pieza>(captureSquare, Color.BLANCO.equals(from.getValue().getColor()) ? Pieza.PEON_NEGRO : Pieza.PEON_BLANCO);
		
		board.setPosicion(captura);			//Devolvemos peon
		board.setPosicion(to);				//Reestablecemos destino
		board.setPosicion(from);			//Volvemos a origen	
	}

}
