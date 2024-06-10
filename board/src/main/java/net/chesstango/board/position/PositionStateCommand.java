package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface PositionStateCommand {
    void doMove(PositionStateWriter positionState);

    void undoMove(PositionStateWriter positionStateWriter);
}
