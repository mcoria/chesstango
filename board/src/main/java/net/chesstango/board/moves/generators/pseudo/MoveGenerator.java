package net.chesstango.board.moves.generators.pseudo;

/**
 * The MoveGenerator interface serves as a central contract for generating all types of pseudo-legal moves in chess.
 * It is designed to consolidate and extend the behavior defined in its parent interfaces:
 * - {@link MoveGeneratorEnPassant}: Responsible for generating en passant capture moves.
 * - {@link MoveGeneratorByPiecePositioned}: Provides logic to generate piece-specific pseudo-legal moves based on their position.
 * - {@link MoveGeneratorCastling}: Handles the generation of castling moves for kings.
 * <p>
 * Implementations of this interface combine these behaviors to provide a comprehensive generator
 * capable of handling chess move generation for any game scenario.
 * <p>
 * Pseudo-legal moves are valid moves for the piece that comply with chess rules for movement,
 * but they may not consider whether the king is left in check. Additional validation is needed
 * to ensure the full legality of the moves.
 * <p>
 * This interface is typically used in move-generation logic within the context of a chess engine.
 *
 * @author Mauricio Coria
 * @see MoveGeneratorEnPassant
 * @see MoveGeneratorByPiecePositioned
 * @see MoveGeneratorCastling
 */
public interface MoveGenerator extends MoveGeneratorEnPassant, MoveGeneratorByPiecePositioned, MoveGeneratorCastling {
}
