package moveexecutors;

import chess.BoardCache;
import chess.BoardState;
import chess.DummyBoard;
import chess.MoveCache;
import chess.PosicionPieza;

public class SimpleMove extends AbstractMove {
	
	public SimpleMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
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
		boardState.pushState();
		boardState.rollTurno();
		boardState.setPeonPasanteSquare(null); 			// Por defecto en null y solo escribimos en SaltoDoblePeonMove
	}
	
	@Override
	public void undoMove(BoardState boardState) {
		boardState.popState();		
	}	
	
	@Override
	public void executeMove(BoardCache boardCache) {
		boardCache.swapPositions(from.getValue().getColor(), from.getKey(), to.getKey());
	}
	
	@Override
	public void undoMove(BoardCache boardCache) {
		boardCache.swapPositions(from.getValue().getColor(), to.getKey(), from.getKey());
	}	
	
	@Override
	public void updateMoveChache(MoveCache moveCache) {
		moveCache.clearPseudoMovesAffectedBy(from.getKey());
		moveCache.clearPseudoMovesAffectedBy(to.getKey());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SimpleMove){
			return true;
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "SimpleMove";
	}
}
