package chess.board.moves.imp;

import chess.board.PiecePositioned;
import chess.board.iterators.Cardinal;
import chess.board.moves.Move;
import chess.board.position.PiecePlacementWriter;
import chess.board.position.imp.MoveCacheBoard;
import chess.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
abstract class AbstractMove implements Move {
	protected final PiecePositioned from;
	protected final PiecePositioned to;
	protected final Cardinal direction;

	public AbstractMove(PiecePositioned from, PiecePositioned to, Cardinal direction) {
		this.from = from;
		this.to = to;
		this.direction = direction;
	}

	public AbstractMove(PiecePositioned from, PiecePositioned to) {
		this.from = from;
		this.to = to;
		this.direction = calculateMoveDirection();
	}	

	@Override
	public PiecePositioned getFrom() {
		return from;
	}

	@Override
	public PiecePositioned getTo() {
		return to;
	}
	
	@Override
	public void executeMove(PiecePlacementWriter board) {
		board.move(from, to);
	}
	
	@Override
	public void undoMove(PiecePlacementWriter board) {
		board.setPosicion(to);							//Reestablecemos destino
		board.setPosicion(from);						//Volvemos a origen
	}	

	//TODO: implementar un decorator antes de crear el movimiento
	@Override
	public void executeMove(PositionState positionState) {
		positionState.pushState();
		positionState.rollTurn();
		positionState.setEnPassantSquare(null); 			// Por defecto en null y solo escribimos en SaltoDoblePawnMove		
	}
	
	@Override
	public void undoMove(PositionState positionState) {
		positionState.popState();		
	}

	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushCleared();
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), true);		
	}

	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), false);
		moveCache.popCleared();
	}
	
	@Override
	public int hashCode() {
		return from.getKey().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Move){
			Move theOther = (Move) obj;
			return from.equals(theOther.getFrom()) &&  to.equals(theOther.getTo());
		}
		return false;
	}

	@Override
	public int compareTo(Move theOther) {
		//Comparamos from
		if(this.from.getKey().getRank() > theOther.getFrom().getKey().getRank()){
			return 1;
		} else if (this.from.getKey().getRank() < theOther.getFrom().getKey().getRank()){
			return -1;
		}
		

		if(this.from.getKey().getFile() <  theOther.getFrom().getKey().getFile()){
			return 1;
		} else if(this.from.getKey().getFile() >  theOther.getFrom().getKey().getFile()){
			return -1;
		}
		
		//---------------
		//Son iguales asi que comparamos to
		if(this.to.getKey().getRank() < theOther.getTo().getKey().getRank()){
			return 1;
		} else if (this.to.getKey().getRank() > theOther.getTo().getKey().getRank()){
			return -1;
		}
		

		if(this.to.getKey().getFile() <  theOther.getTo().getKey().getFile()){
			return -1;
		} else if(this.to.getKey().getFile() >  theOther.getTo().getKey().getFile()){
			return 1;
		}
		
		//--------------- Desde y hasta coinciden, que hacemos ?
		
		return 0;
	}

	@Override
	public String toString() {
		return from.toString() + " " + to.toString() + " - " + this.getClass().getSimpleName();
	}


	@Override
	public Cardinal getMoveDirection() {
		return direction;
	}

	public Cardinal calculateMoveDirection() {
		for(Cardinal direction: Cardinal.values()){
			if(direction.isInDirection(getFrom().getKey(), getTo().getKey())){
				return direction;
			}
		}
		return null;
	}

}
