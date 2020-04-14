package moveexecutors;

import java.util.List;
import java.util.Map.Entry;

import chess.BoardState;
import chess.Board;
import chess.Pieza;
import chess.Square;

public class CapturePeonPasante extends AbstractMove {

	private final Entry<Square, Pieza> captura;
			
	public CapturePeonPasante(Entry<Square, Pieza> from, Entry<Square, Pieza> to, Entry<Square, Pieza> captura) {
		super(from, to);
		this.captura = captura;
	}
	
	@Override
	public void executeMove(Board board) {
		board.setEmptySquare(from.getKey()); 						//Dejamos el origen
		board.setPieza(to.getKey(), from.getValue());				//Vamos al destino
		board.setEmptySquare(captura.getKey());						//Capturamos peon
	}

	@Override
	public void undoMove(Board board) {	
		board.setPosicion(captura);			//Devolvemos peon
		board.setPosicion(to);				//Reestablecemos destino
		board.setPosicion(from);			//Volvemos a origen	
	}
	
	@Override
	public void executeState(BoardState boardState) {
		boardState.pushState();
		boardState.setPeonPasanteSquare(null);	
		boardState.rollTurno();
	}
	
	@Override
	public void executeSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		squaresTurno.remove(from.getKey());
		squaresTurno.add(to.getKey());
		
		squaresOpenente.remove(captura.getKey());
	}

	@Override
	public void undoSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		squaresTurno.remove(to.getKey());
		squaresTurno.add(from.getKey());
		
		squaresOpenente.add(captura.getKey());
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
