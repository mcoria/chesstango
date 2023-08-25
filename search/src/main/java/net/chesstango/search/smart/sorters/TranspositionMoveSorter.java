package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionMoveSorter implements MoveSorter {
    private static final MoveComparator moveComparator = new MoveComparator();
    private final TranspositionEntry storage = new TranspositionEntry();
    private Game game;
    private TTable tTable;
    private TTable qTTable;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.tTable = context.getTTable();
        this.qTTable = context.getQTTable();
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

        short bestMoveEncoded = 0;

        if(tTable.read(hash, storage)){
            bestMoveEncoded = BinaryUtils.decodeMove(storage.getBestMoveAndValue());
        } else if (qTTable.read(hash, storage)) {
            bestMoveEncoded = BinaryUtils.decodeMove(storage.getBestMoveAndValue());
        }

        List<Move> sortedMoveList = new LinkedList<>();

        if (bestMoveEncoded != 0) {
            Move bestMove = null;
            List<Move> unsortedMoveList = new LinkedList<>();
            for (Move move : game.getPossibleMoves()) {
                if (move.binaryEncoding() == bestMoveEncoded) {
                    bestMove = move;
                } else {
                    unsortedMoveList.add(move);
                }
            }

            Collections.sort(unsortedMoveList, moveComparator.reversed());

            sortedMoveList.add(bestMove);
            sortedMoveList.addAll(unsortedMoveList);

        } else {
            game.getPossibleMoves().forEach(sortedMoveList::add);

            Collections.sort(sortedMoveList, moveComparator.reversed());
        }

        return sortedMoveList;
    }
}
