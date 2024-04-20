package net.chesstango.board.moves.generators.pseudo;

import lombok.Getter;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveList;


/**
 * @author Mauricio Coria
 *
 */
@Getter
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

	public MoveGeneratorResult addPseudoMove(Move move) {
		pseudoMoves.add(move);
		return this;
	}

	public MoveGeneratorResult addAffectedByPositions(Square key) {
		affectedByPositions |= key.getBitPosition();
		return this;
	}

	public MoveGeneratorResult addCapturedPositions(Square key) {
		capturedPositions |= key.getBitPosition();
		return this;
	}

	@Override
	public String toString() {
		return pseudoMoves.toString();
	}	
}
