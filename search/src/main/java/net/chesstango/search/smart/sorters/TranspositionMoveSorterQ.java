package net.chesstango.search.smart.sorters;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionMoveSorterQ implements MoveSorter {
    private static final MoveComparator moveComparator = new MoveComparator();
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
        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry;
        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            entry = maxMap.get(hash);
        } else {
            entry = minMap.get(hash);
        }

        short bestMoveEncoded = 0;
        short secondBestMoveEncoded = 0;

        if (entry != null) {
            bestMoveEncoded = TranspositionEntry.decodeBestMove(entry.movesAndValue);
            secondBestMoveEncoded = TranspositionEntry.decodeSecondBestMove(entry.movesAndValue);
        }

        List<Move> sortedMoveList = new LinkedList<>();

        if (bestMoveEncoded != 0) {
            Move bestMove = null;
            Move secondBestMove = null;
            List<Move> unsortedMoveList = new LinkedList<>();
            for (Move move : game.getPossibleMoves()) {
                if (!move.isQuiet()) {
                    short encodedMove = move.binaryEncoding();
                    if (encodedMove == bestMoveEncoded) {
                        bestMove = move;
                    } else if (encodedMove == secondBestMoveEncoded) {
                        secondBestMove = move;
                    } else {
                        unsortedMoveList.add(move);
                    }
                }
            }

            if (bestMove != null) {
                sortedMoveList.add(bestMove);
            }
            if (secondBestMove != null) {
                sortedMoveList.add(secondBestMove);
            }

            unsortedMoveList.sort(moveComparator.reversed());
            sortedMoveList.addAll(unsortedMoveList);

        } else {
            game.getPossibleMoves().forEach(move -> {
                if (!move.isQuiet()) {
                    sortedMoveList.add(move);
                }
            });

            sortedMoveList.sort(moveComparator.reversed());
        }

        return sortedMoveList;
    }
}
