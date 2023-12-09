package net.chesstango.search.smart.sorters;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionMoveSorter implements MoveSorter, SearchByDepthListener {
    private static final MoveComparator moveComparator = new MoveComparator();
    private Game game;
    private TTable maxMap;
    private TTable minMap;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void afterSearch() {

    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public List<Move> getSortedMoves() {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = Color.WHITE.equals(currentTurn) ?
                maxMap.getForRead(hash) : minMap.getForRead(hash);

        short bestMoveEncoded = 0;
        if (entry != null) {
            bestMoveEncoded = TranspositionEntry.decodeBestMove(entry.movesAndValue);
        }

        List<Move> sortedMoveList = new LinkedList<>();
        Move bestMove = null;
        List<Move> unsortedMoveList = new LinkedList<>();
        List<MoveEvaluation> unsortedMoveValueList = new LinkedList<>();
        for (Move move : game.getPossibleMoves()) {
            short encodedMove = move.binaryEncoding();
            if (encodedMove == bestMoveEncoded) {
                bestMove = move;
            } else {
                long zobristHashMove = game.getChessPosition().getZobristHash(move);

                TranspositionEntry moveEntry = Color.WHITE.equals(currentTurn) ?
                        minMap.getForRead(zobristHashMove) : maxMap.getForRead(zobristHashMove);

                if (moveEntry != null) {
                    int moveValue = TranspositionEntry.decodeValue(moveEntry.movesAndValue);
                    unsortedMoveValueList.add(new MoveEvaluation(move, moveValue));
                } else {
                    unsortedMoveList.add(move);
                }
            }
        }

        if (bestMove != null) {
            sortedMoveList.add(bestMove);
        }

        if (!unsortedMoveValueList.isEmpty()) {
            unsortedMoveValueList.sort(Color.WHITE.equals(currentTurn) ? Comparator.reverseOrder() : Comparator.naturalOrder());
            unsortedMoveValueList.stream().map(MoveEvaluation::move).forEach(sortedMoveList::add);
        }

        if (!unsortedMoveList.isEmpty()) {
            unsortedMoveList.sort(moveComparator.reversed());
            sortedMoveList.addAll(unsortedMoveList);
        }

        return sortedMoveList;
    }
}
