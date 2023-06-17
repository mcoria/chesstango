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

	private long affectedByContainer;
	
	private long capturedPositions;

	public MoveGeneratorResult(PiecePositioned from) {
		this.pseudoMoves = new MoveList();
		this.from = from;
		this.affectedByContainer = from.getSquare().getBitPosition();
	}

	public MoveGeneratorResult addPseudoMove(Move move) {
		pseudoMoves.add(move);
		return this;
	}

	public MoveList getPseudoMoves(){
		return pseudoMoves;
	}

	public MoveGeneratorResult addAffectedByPositions(Square key) {
		affectedByContainer |= key.getBitPosition();
		return this;
	}
	public long getAffectedByPositions() {
		return affectedByContainer;
	}

	public MoveGeneratorResult addCapturedPositions(Square key) {
		capturedPositions |= key.getBitPosition();
		return this;
	}
	public long getCapturedPositions() {
		return capturedPositions;
	}

	public PiecePositioned getFrom() {
		return from;
	}

	@Override
	public String toString() {
		return pseudoMoves.toString();
	}	
}
