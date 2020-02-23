package moveexecutors;

import java.util.Map.Entry;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class CapturePeonPasante extends AbstractMove {

	private final Entry<Square, Pieza> captura;
			
	public CapturePeonPasante(Entry<Square, Pieza> from, Entry<Square, Pieza> to, Entry<Square, Pieza> captura) {
		super(from, to);
		this.captura = captura;
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		board.setEmptySquare(from.getKey()); 						//Dejamos el origen
		board.setPieza(to.getKey(), from.getValue());				//Vamos al destino
		board.setEmptySquare(captura.getKey());						//Capturamos peon
	}

	@Override
	public void undoMove(DummyBoard board) {	
		board.setPosicion(captura);			//Devolvemos peon
		board.setPosicion(to);				//Reestablecemos destino
		board.setPosicion(from);			//Volvemos a origen	
	}
	
	@Override
	public void executeState(BoardState boardState) {
		boardState.setPeonPasanteSquare(null);	
		boardState.rollTurno();
	}

	@Override
	public void undoState(BoardState boardState) {
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj) && obj instanceof CapturePeonPasante){
			CapturePeonPasante theOther = (CapturePeonPasante) obj;
			return captura.equals(theOther.captura) ;
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "CapturePeonPasante";
	}	

}
