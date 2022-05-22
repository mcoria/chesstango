package chess.board.pseudomovesgenerators;

import java.util.Collection;

import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveList;


/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorResult {
	
	private PiecePositioned from;
	
	private final Collection<Move> moveContainer;
	
	private long affectedByContainer;
	
	private long capturedPositions;

	public MoveGeneratorResult() {
		moveContainer = new MoveList();
	}
	
	public Collection<Move> getPseudoMoves(){
		return moveContainer;
	}

	public long getAffectedBy() {
		return affectedByContainer;
	}
	
	public void affectedByContainerAdd(Square key) {
		affectedByContainer |= key.getPosicion();
	}
	
	public void capturedPositionsContainerAdd(Square key) {
		capturedPositions |= key.getPosicion();
	}		
	
	public void moveContainerAdd(Move move) {
		moveContainer.add(move);
	}

	public PiecePositioned getFrom() {
		return from;
	}

	public void setFrom(PiecePositioned from) {
		this.from = from;
	}
	
	public long getCapturedPositions() {
		return capturedPositions;
	}
	
	@Override
	public String toString() {
		return moveContainer.toString();
	}	
}
