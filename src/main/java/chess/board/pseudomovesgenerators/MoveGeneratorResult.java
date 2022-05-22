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
	
	private final PiecePositioned from;
	
	private final MoveList moveContainer;
	
	private long affectedByContainer;
	
	private long capturedPositions;

	public MoveGeneratorResult(PiecePositioned from) {
		moveContainer = new MoveList();
		this.from = from;
	}
	
	public MoveList getPseudoMoves(){
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
	
	public long getCapturedPositions() {
		return capturedPositions;
	}
	
	@Override
	public String toString() {
		return moveContainer.toString();
	}	
}
