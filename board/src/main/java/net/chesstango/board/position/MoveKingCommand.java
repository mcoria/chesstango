package net.chesstango.board.position;

/**
 * Interface representing a specialized move command exclusively for a king.
 * It extends both {@link MoveCommand} and {@link KingSquareCommand},
 * combining their functionalities to handle king-specific moves.
 *
 * @author Mauricio Coria
 */
public interface MoveKingCommand extends
        MoveCommand, // Base interface for a move command
        KingSquareCommand { // Interface for manipulating a king's square
}
