package net.chesstango.board.moves.generators.pseudo;

import lombok.Getter;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.moves.PseudoMove;


/**
 * This class represents the result of generating pseudo-legal chess moves for a specific piece.
 * <p>
 * It encapsulates the piece's position, generated pseudo-legal moves, and additional information
 * related to the bitboard representation of affected and captured positions.
 * <p>
 * Pseudo-legal moves are moves that are valid according to the rules of chess but may leave the king in check.
 *
 * @author Mauricio Coria
 */
@Getter
public class MoveGeneratorResult {

	/**
	 * This represents the piece for which pseudo-legal moves are being calculated.
	 */
	private final PiecePositioned from;

	/**
	 * A dynamically generated list of pseudo-legal moves for the piece located at the position.
	 * This list is initialized as empty and populated as moves are added.
	 */
	private final MoveList<PseudoMove> pseudoMoves;

	/**
	 * A bitboard representation indicating all squares whose positions influence the moves 
	 * of the current piece. It is updated based on game rules.
	 */
	private long affectedByPositions;

	/**
	 * A bitboard representation indicating all squares where capturing moves are possible.
	 * Capturing moves occur when the piece can take an opponent's piece.
	 */
	private long capturedPositions;

	/**
	 * Initializes an instance to store pseudo-legal moves and related information
	 * for the specified piece and its position.
	 *
	 * @param from The piece and its position for which pseudo-legal moves are generated.
	 */
	public MoveGeneratorResult(PiecePositioned from) {
		this.pseudoMoves = new MoveList<>();
		this.from = from;
		this.affectedByPositions = from.getSquare().getBitPosition();
	}

	/**
	 * Adds a pseudo-legal move to the list of moves associated with the current piece.
	 * The move must conform to the basic rules of the piece but may leave the king in check.
	 *
	 * @param move The pseudo-legal move to add.
	 */
	public void addPseudoMove(PseudoMove move) {
		pseudoMoves.add(move);
	}

	/**
	 * Updates the bitboard to include the position represented by the specified square.
	 * These affected positions help track the squares influencing the piece's moves.
	 *
	 * @param key The square to add to the affected positions bitboard.
	 */
	public void addAffectedByPositions(Square key) {
		affectedByPositions |= key.getBitPosition();
	}

	/**
	 * Updates the bitboard to include the position represented by the specified square
	 * as a potential capturing position. These positions are where the current piece
	 * might capture an opponent's piece.
	 *
	 * @param key The square to mark as a potential capturing position.
	 */
	public void addCapturedPositions(Square key) {
		capturedPositions |= key.getBitPosition();
	}

	/**
	 * Returns a string representation of the pseudo-legal moves generated for the piece.
	 * This includes all moves currently stored in the move list.
	 *
	 * @return A string listing all pseudo-legal moves for the piece.
	 */
	@Override
	public String toString() {
		return pseudoMoves.toString();
	}
}
