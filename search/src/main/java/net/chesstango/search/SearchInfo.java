package net.chesstango.search;

/**
 * @author Mauricio Coria
 */
public record SearchInfo(SearchMoveResult searchMoveResult, long timeSearching, long timeSearchingLastDepth) {
}
