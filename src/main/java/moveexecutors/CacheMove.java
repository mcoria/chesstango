package moveexecutors;

import chess.BoardCache;
import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.PosicionPieza;

//Lo camos a utilizar para evitar construir los objetos de movimiento de antemano
public final class CacheMove implements Move {
	
	private PosicionPieza from;
	private PosicionPieza to;
	
	public CacheMove(){
		
	}

	public void setFromTo(PosicionPieza from, PosicionPieza to){
		this.from =  from;
		this.to = to;
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		board.move(from, to);
	}
	
	@Override
	public void undoMove(DummyBoard board) {
		board.setPosicion(to);							//Reestablecemos destino
		board.setPosicion(from);						//Volvemos a origen
	}

	@Override
	public void executeMove(BoardState boardState) {
		throw new RuntimeException("Mothod not implemented");
	}

	@Override
	public void undoMove(BoardState boardState) {
		throw new RuntimeException("Mothod not implemented");
	}

	@Override
	public void executeMove(BoardCache boardCache) {
		throw new RuntimeException("Mothod not implemented");
	}

	@Override
	public void undoMove(BoardCache boardCache) {
		throw new RuntimeException("Mothod not implemented");
	}

	@Override
	public PosicionPieza getFrom() {
		return from;
	}

	@Override
	public PosicionPieza getTo() {
		return to;
	}

}
