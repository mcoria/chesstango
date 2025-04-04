package net.chesstango.board.moves.generators.pseudo;

import net.chesstango.board.PiecePositioned;


/**
 *
 * This interface defines the contract for generating pseudo-legal moves for specific chess
 * pieces based on their position. Pseudo-legal moves are valid for the piece but may leave
 * the king in check, so additional validation is required to determine legality.
 *
 * Castling and En Passant moves are not generated.
 *
 * @author Mauricio Coria
 */
public interface MoveGeneratorByPiece {


	/**
	 * The generateByPiecePseudoMoves() method does not generate castling or en passant pseudo-moves because it is
	 * designed to generate pseudo-legal moves for specific chess pieces based on their position.
	 * Castling and en passant are special moves in chess that involve specific conditions and rules:
	 * <p>
	 * Castling: This move involves both the king and a rook and has specific conditions such as neither piece having
	 * moved before, no pieces between them, and the king not being in check or passing through check. These conditions are not handled by the general piece move generation logic.
	 * <p>
	 * En Passant: This move is a special pawn capture that can only occur immediately after an opponent's pawn moves
	 * two squares forward from its starting position. It requires tracking the opponent's last move and specific
	 * conditions that are not part of the standard piece move generation.
	 * <p>
	 * Therefore, the generateByPiecePseudoMoves() method focuses on generating moves that are valid for individual
	 * pieces without considering these special move conditions. Special methods or classes are typically used to
	 * handle the generation of castling and en passant moves separately.
	 *
	 * @param from The position and type of the piece for which to generate moves.
	 * @return All pseudo-legal moves for the specified piece.
	 */
	MoveGeneratorByPieceResult generateByPiecePseudoMoves(PiecePositioned from);

}
