package chess.board.movesgenerators.pseudo;

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

	private final MoveList pseudoMoves;

	private long affectedByContainer;
	
	private long capturedPositions;

	public MoveGeneratorResult(PiecePositioned from) {
		this.pseudoMoves = new MoveList();
		this.from = from;
	}

	public void addPseudoMove(Move move) {
		pseudoMoves.add(move);
	}

	public MoveList getPseudoMoves(){
		return pseudoMoves;
	}

	public void addAffectedByPositions(Square key) {
		affectedByContainer |= key.getBitPosition();
	}
	public long getAffectedByPositions() {
		return affectedByContainer;
	}

	public void addCapturedPositions(Square key) {
		capturedPositions |= key.getBitPosition();
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
