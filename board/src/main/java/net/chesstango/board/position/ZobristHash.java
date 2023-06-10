package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface ZobristHash extends ZobristHashReader, ZobristHashWriter {

    void init(ChessPositionReader piecePlacement);

    void init(BoardReader piecePlacement, PositionStateReader positionState);

}
