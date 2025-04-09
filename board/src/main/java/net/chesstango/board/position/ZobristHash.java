package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface ZobristHash extends ZobristHashReader, ZobristHashWriter {

    void init(PositionReader positionReader);

    void init(SquareBoardReader piecePlacement, PositionStateReader positionState);

    void restoreSnapshot(ZobristHashReader zobristHashSnapshot);

    ZobristHashReader takeSnapshot();
}
