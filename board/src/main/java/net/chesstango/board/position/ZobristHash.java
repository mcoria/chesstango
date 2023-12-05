package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface ZobristHash extends ZobristHashReader, ZobristHashWriter {

    void init(ChessPositionReader chessPositionReader);

    void init(SquareBoardReader piecePlacement, PositionStateReader positionState);

}
