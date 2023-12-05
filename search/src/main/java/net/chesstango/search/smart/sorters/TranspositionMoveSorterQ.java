package net.chesstango.search.smart.sorters;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionMoveSorterQ implements MoveSorter {
    private static final MoveComparator moveComparator = new MoveComparator();
    private static final MoveAndValueComparator moveAndValueComparator = new MoveAndValueComparator();
    private Game game;
    private TTable maxMap;
    private TTable minMap;

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxMap = context.getQMaxMap();
        this.minMap = context.getQMinMap();
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
        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = (Color.WHITE.equals(currentTurn)) ?
                maxMap.getForRead(hash) : minMap.getForRead(hash);

        short bestMoveEncoded = 0;
        short secondBestMoveEncoded = 0;
        if (entry != null) {
            bestMoveEncoded = TranspositionEntry.decodeBestMove(entry.movesAndValue);
            secondBestMoveEncoded = TranspositionEntry.decodeSecondBestMove(entry.movesAndValue);
        }

        List<Move> sortedMoveList = new LinkedList<>();
        Move bestMove = null;
        Move secondBestMove = null;
        List<Move> unsortedMoveList = new LinkedList<>();
        List<MoveAndValue> unsortedMoveValueList = new LinkedList<>();
        for (Move move : game.getPossibleMoves()) {
            if (!move.isQuiet()) {
                short encodedMove = move.binaryEncoding();
                if (encodedMove == bestMoveEncoded) {
                    bestMove = move;
                } else if (encodedMove == secondBestMoveEncoded) {
                    secondBestMove = move;
                } else {
                    long zobristHashMove = game.getChessPosition().getZobristHash(move);

                    TranspositionEntry moveEntry = Color.WHITE.equals(currentTurn) ?
                            minMap.getForRead(zobristHashMove) : maxMap.getForRead(zobristHashMove);

                    if (moveEntry != null) {
                        int moveValue = TranspositionEntry.decodeValue(moveEntry.movesAndValue);
                        unsortedMoveValueList.add(new MoveAndValue(move, moveValue));
                    } else {
                        unsortedMoveList.add(move);
                    }
                }
            }
        }

        if (bestMove != null) {
            sortedMoveList.add(bestMove);
            if (secondBestMove != null) {
                sortedMoveList.add(secondBestMove);
            }
        }

        if (!unsortedMoveValueList.isEmpty()) {
            unsortedMoveValueList.sort(Color.WHITE.equals(currentTurn) ? moveAndValueComparator.reversed() : moveAndValueComparator);
            unsortedMoveValueList.stream().map(MoveAndValue::move).forEach(sortedMoveList::add);
        }

        if (!unsortedMoveList.isEmpty()) {
            unsortedMoveList.sort(moveComparator.reversed());
            sortedMoveList.addAll(unsortedMoveList);
        }

        return sortedMoveList;
    }

    private record MoveAndValue(Move move, int value) {
    }

    private static class MoveAndValueComparator implements Comparator<MoveAndValue> {
        @Override
        public int compare(MoveAndValue o1, MoveAndValue o2) {
            return Integer.compare(o1.value, o2.value);
        }
    }
}
