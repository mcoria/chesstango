package net.chesstango.board.position;

/**
 * Chess position state:
 *
 * <ul>
 * <li>Side to move</li>
 * <li>Castling Rights</li>
 * <li>En passant target square</li>
 * <li>FullMoveClock</li>
 * <li>Halfmove Clock</li>
 *
 * @author Mauricio Coria
 */
public interface PositionState extends PositionStateReader, PositionStateWriter {
}
