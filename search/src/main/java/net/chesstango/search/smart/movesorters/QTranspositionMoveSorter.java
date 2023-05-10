package net.chesstango.search.smart.movesorters;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.Quiescence;

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
    private Map<Long, SearchContext.TableEntry> qMaxMap;
    private Map<Long, SearchContext.TableEntry> qMinMap;

    @Override
    public void init(Game game, SearchContext context) {
        this.game = game;
        this.qMaxMap = context.getQMaxMap();
        this.qMinMap = context.getQMinMap();
    }

    @Override
    public void close(SearchMoveResult result) {

    }

    @Override
    public List<Move> getSortedMoves() {
        long hash = game.getChessPosition().getPositionHash();

        SearchContext.TableEntry entry;
        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            entry = qMaxMap.get(hash);
        } else {
            entry = qMinMap.get(hash);
        }

        short bestMoveEncoded = entry != null ? BinaryUtils.decodeMove(entry.bestMoveAndValue) : 0;

        List<Move> sortedMoveList = new LinkedList<>();

        if (bestMoveEncoded != 0) {
            Move bestMove = null;
            List<Move> unsortedMoveList = new LinkedList<>();
            for (Move move : game.getPossibleMoves()) {
                if (move.binaryEncoding() == bestMoveEncoded) {
                    bestMove = move;
                } else {
                    if(Quiescence.isNotQuiet(move)){
                        unsortedMoveList.add(move);
                    }
                }
            }

            Collections.sort(unsortedMoveList, moveComparator.reversed());

            sortedMoveList.add(bestMove);
            sortedMoveList.addAll(unsortedMoveList);

        } else {
            game.getPossibleMoves().forEach( move ->{
                if(Quiescence.isNotQuiet(move)){
                    sortedMoveList.add(move);
                }
            });

            Collections.sort(sortedMoveList, moveComparator.reversed());
        }

        return sortedMoveList;
    }
}
