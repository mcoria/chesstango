package net.chesstango.board.moves.generators.pseudo;

import net.chesstango.board.PiecePositioned;


/**
 *
 * This interface defines the contract for generating pseudo-legal moves for specific chess
 * pieces based on their position. Pseudo-legal moves are valid for the piece but may leave
 * the king in check, so additional validation is required to determine legality.
 *
 * @author Mauricio Coria
 */
public interface MoveGeneratorByPiece {


	/**
	 * Generates all possible pseudo-legal moves for a given chess piece at the specified position.
	 * Pseudo-legal means the moves are valid for the piece regardless of whether they place
	 * the king in check. This method does not handle validation for check conditions.
	 *
	 * @param origen The position and type of the piece for which to generate moves.
	 * @return All pseudo-legal moves for the specified piece.
	 */
	MoveGeneratorByPieceResult generatePseudoMoves(PiecePositioned origen);

}
