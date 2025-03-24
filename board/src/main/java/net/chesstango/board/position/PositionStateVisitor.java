package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface PositionStateVisitor {
    void doMove(PositionStateWriter positionState);

    void undoMove(PositionStateWriter positionStateWriter);
}
