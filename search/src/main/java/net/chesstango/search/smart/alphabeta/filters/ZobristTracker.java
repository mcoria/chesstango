package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;

import java.util.*;

/**
 * @author Mauricio Coria
 */
public class ZobristTracker implements AlphaBetaFilter {
    private Map<Long, String> maxMap = new HashMap<>();
    private Map<Long, String> minMap = new HashMap<>();
    private List<String> collisions = new LinkedList<>();
    private Game game;

    private AlphaBetaFilter next;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {
        maxMap.clear();
        minMap.clear();
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        findCollision(maxMap);
        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        findCollision(minMap);
        return next.minimize(currentPly, alpha, beta);
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    protected void findCollision(Map<Long, String> theMap) {
        FENEncoder encoder = new FENEncoder();

        ChessPositionReader chessPosition = game.getChessPosition();

        chessPosition.constructChessPositionRepresentation(encoder);
        String fenWithoutClocks = encoder.getFENWithoutClocks();

        long hash = chessPosition.getZobristHash();

        String old = theMap.put(hash, fenWithoutClocks);
        if (Objects.nonNull(old) && !Objects.equals(old, fenWithoutClocks)) {
            collisions.add(old);
        }
    }
}