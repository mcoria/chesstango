package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.*;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class TranspositionMoveSorter implements MoveSorterElement, SearchByCycleListener {
    private final Function<SearchByCycleContext, TTable> fnGetMaxMap;
    private final Function<SearchByCycleContext, TTable> fnGetMinMap;

    @Getter
    @Setter
    private MoveSorterElement next;
    private Game game;
    private TTable maxMap;
    private TTable minMap;

    public TranspositionMoveSorter(Function<SearchByCycleContext, TTable> fnGetMaxMap, Function<SearchByCycleContext, TTable> fnGetMinMap) {
        this.fnGetMaxMap = fnGetMaxMap;
        this.fnGetMinMap = fnGetMinMap;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.maxMap = fnGetMaxMap.apply(context);
        this.minMap = fnGetMinMap.apply(context);
    }

    @Override
    public void afterSearch() {
    }

    @Override
    public void sort(List<Move> unsortedMoves, List<Move> sortedMoves) {
        final List<MoveEvaluation> unsortedMoveEvaluations = new LinkedList<>();

        final Color currentTurn = game.getChessPosition().getCurrentTurn();

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = Color.WHITE.equals(currentTurn) ?
                maxMap.read(hash) : minMap.read(hash);

        short bestMoveEncoded = Objects.nonNull(entry) ? TranspositionEntry.decodeBestMove(entry.movesAndValue) : 0;

        Iterator<Move> moveIterator = unsortedMoves.iterator();
        while (moveIterator.hasNext()) {
            Move move = moveIterator.next();
            short encodedMove = move.binaryEncoding();
            if (encodedMove == bestMoveEncoded) {
                sortedMoves.add(move);
                moveIterator.remove();
            } else {
                long zobristHashMove = game.getChessPosition().getZobristHash(move);

                TranspositionEntry moveEntry = Color.WHITE.equals(currentTurn) ?
                        minMap.read(zobristHashMove) : maxMap.read(zobristHashMove);

                if (moveEntry != null) {
                    int moveValue = TranspositionEntry.decodeValue(moveEntry.movesAndValue);

                    MoveEvaluationType moveEvaluationType = switch (moveEntry.transpositionBound) {
                        case EXACT -> MoveEvaluationType.EXACT;
                        case UPPER_BOUND -> MoveEvaluationType.UPPER_BOUND;
                        case LOWER_BOUND -> MoveEvaluationType.LOWER_BOUND;
                    };

                    unsortedMoveEvaluations.add(new MoveEvaluation(move, moveValue, moveEvaluationType));

                    moveIterator.remove();
                }
            }
        }

        if (!unsortedMoveEvaluations.isEmpty()) {
            unsortedMoveEvaluations.sort(Color.WHITE.equals(currentTurn) ? Comparator.reverseOrder() : Comparator.naturalOrder());
            unsortedMoveEvaluations.stream().map(MoveEvaluation::move).forEach(sortedMoves::add);
        }

        next.sort(unsortedMoves, sortedMoves);
    }
}
