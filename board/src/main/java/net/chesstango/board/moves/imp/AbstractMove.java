package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.PiecePlacementWriter;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractMove implements Move {
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
		positionState.incrementFullMoveClock();
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
		moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), true);
	}

	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), false);
		moveCache.popCleared();
	}


	@Override
	public void executeMove(ZobristHash hash) {
		hash.xorPosition(from);
		hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));
		hash.xorTurn();
	}

	@Override
	public void undoMove(ZobristHash hash) {
		executeMove(hash);
	}

	@Override
	public int hashCode() {
		return from.getSquare().hashCode();
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
		if(this.from.getSquare().getRank() > theOther.getFrom().getSquare().getRank()){
			return 1;
		} else if (this.from.getSquare().getRank() < theOther.getFrom().getSquare().getRank()){
			return -1;
		}
		

		if(this.from.getSquare().getFile() <  theOther.getFrom().getSquare().getFile()){
			return 1;
		} else if(this.from.getSquare().getFile() >  theOther.getFrom().getSquare().getFile()){
			return -1;
		}
		
		//---------------
		//Son iguales asi que comparamos to
		if(this.to.getSquare().getRank() < theOther.getTo().getSquare().getRank()){
			return 1;
		} else if (this.to.getSquare().getRank() > theOther.getTo().getSquare().getRank()){
			return -1;
		}
		

		if(this.to.getSquare().getFile() <  theOther.getTo().getSquare().getFile()){
			return -1;
		} else if(this.to.getSquare().getFile() >  theOther.getTo().getSquare().getFile()){
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

	private Cardinal calculateMoveDirection() {
		return Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
	}

}
