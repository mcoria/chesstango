package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface PositionState extends PositionStateReader, PositionStateWriter {

    PositionStateReader takePositionStateSnapshot();
}
