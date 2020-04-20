package moveexecutors;

import java.util.List;

import chess.DummyBoard;
import chess.PosicionPieza;
import chess.Square;

public class CapturePeonPasante extends AbstractMove {

	private final PosicionPieza captura;
			
	public CapturePeonPasante(PosicionPieza from, PosicionPieza to, PosicionPieza captura) {
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
	public void executeSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		super.executeSquareLists(squaresTurno, squaresOpenente);
		
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
