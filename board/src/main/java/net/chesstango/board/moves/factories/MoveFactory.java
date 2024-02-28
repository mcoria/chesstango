package net.chesstango.board.moves.factories;

/**
 * @author Mauricio Coria
 */
public interface MoveFactory extends
        PawnMoveFactory,
        RookMoveFactory,
        BishopMoveFactory,
        KnightMoveFactory,
        QueenMoveFactory,
        KingMoveFactory {
}