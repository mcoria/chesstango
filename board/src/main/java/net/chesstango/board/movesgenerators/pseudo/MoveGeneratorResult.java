package net.chesstango.board.movesgenerators.pseudo;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveList;


/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorResult {
	private final PiecePositioned from;

	private final MoveList pseudoMoves;

	private long affectedByPositions;
	
	private long capturedPositions;

	public MoveGeneratorResult(PiecePositioned from) {
		this.pseudoMoves = new MoveList();
		this.from = from;
		this.affectedByPositions = from.getSquare().getBitPosition();
	}

	public void setAffectedByPositions(long affectedByPositions) {
		this.affectedByPositions = affectedByPositions;
	}

	public long getAffectedByPositions() {
		return affectedByPositions;
	}
	public void setCapturedPositions(long capturedPositions) {
		this.capturedPositions = capturedPositions;
	}
	public long getCapturedPositions() {
		return capturedPositions;
	}

	public MoveGeneratorResult addPseudoMove(Move move) {
		pseudoMoves.add(move);
		return this;
	}

	public MoveList getPseudoMoves(){
		return pseudoMoves;
	}

	public MoveGeneratorResult addAffectedByPositions(Square key) {
		affectedByPositions |= key.getBitPosition();
		return this;
	}

	public MoveGeneratorResult addCapturedPositions(Square key) {
		capturedPositions |= key.getBitPosition();
		return this;
	}

	public PiecePositioned getFrom() {
		return from;
	}

	@Override
	public String toString() {
		return pseudoMoves.toString();
	}	
}
