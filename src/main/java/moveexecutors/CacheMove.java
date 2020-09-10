package moveexecutors;

import chess.BoardState;
import chess.Move;
import chess.MoveCache;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.DummyBoard;

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
	public PosicionPieza getFrom() {
		return from;
	}

	@Override
	public PosicionPieza getTo() {
		return to;
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
	public void executeMove(ColorBoard boardCache) {
		throw new RuntimeException("Mothod not implemented");
	}

	@Override
	public void undoMove(ColorBoard boardCache) {
		throw new RuntimeException("Mothod not implemented");
	}

	@Override
	public void executeMove(MoveCache moveCache) {
		throw new RuntimeException("Mothod not implemented");
		
	}

	@Override
	public void undoMove(MoveCache moveCache) {
		throw new RuntimeException("Mothod not implemented");
	}

}
