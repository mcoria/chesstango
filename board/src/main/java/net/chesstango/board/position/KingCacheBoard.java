package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface KingCacheBoard extends KingCacheBoardReader, KingCacheBoardWriter {
    void init(BoardReader board);

}
