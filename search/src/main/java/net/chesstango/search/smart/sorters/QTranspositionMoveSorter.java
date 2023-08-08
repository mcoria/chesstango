package net.chesstango.search.smart.sorters;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.Transposition;
import net.chesstango.search.smart.alphabeta.filters.Quiescence;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class QTranspositionMoveSorter implements MoveSorter {
    private static final MoveComparator moveComparator = new MoveComparator();
    private Game game;
    private Map<Long, Transposition> maxMap;
    private Map<Long, Transposition> minMap;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    @Override
    public List<Move> getSortedMoves() {
        long hash = game.getChessPosition().getZobristHash();

        Transposition entry;
        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            entry = maxMap.get(hash);
        } else {
            entry = minMap.get(hash);
        }

        short bestMoveEncoded = (entry != null && entry.qBestMoveAndValue != 0)  ? BinaryUtils.decodeMove(entry.qBestMoveAndValue) : 0;

        List<Move> sortedMoveList = new LinkedList<>();

        if (bestMoveEncoded != 0) {
            Move bestMove = null;
            List<Move> unsortedMoveList = new LinkedList<>();
            for (Move move : game.getPossibleMoves()) {
                if (move.binaryEncoding() == bestMoveEncoded) {
                    bestMove = move;
                } else {
                    if (Quiescence.isNotQuiet(move)) {
                        unsortedMoveList.add(move);
                    }
                }
            }

            Collections.sort(unsortedMoveList, moveComparator.reversed());

            sortedMoveList.add(bestMove);
            sortedMoveList.addAll(unsortedMoveList);

        } else {
            game.getPossibleMoves().forEach(move -> {
                if (Quiescence.isNotQuiet(move)) {
                    sortedMoveList.add(move);
                }
            });

            Collections.sort(sortedMoveList, moveComparator.reversed());
        }

        return sortedMoveList;
    }
}
