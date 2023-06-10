package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.BoardReader;

/**
 * @author Mauricio Coria
 *
 */
public interface KingCacheBoard extends KingCacheBoardReader, KingCacheBoardWriter {
    void init(BoardReader board);

}
