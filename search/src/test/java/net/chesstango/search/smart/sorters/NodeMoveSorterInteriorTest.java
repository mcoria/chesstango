package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Bound;
import net.chesstango.search.builders.EvaluationBuilder;
import net.chesstango.search.builders.KillerMoveBuilder;
import net.chesstango.search.builders.TranspositionTableBuilder;
import net.chesstango.search.builders.sorters.MoveSorterBuilder;
import net.chesstango.search.builders.sorters.MoveSorterInteriorBuilder;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.killermoves.KillerMoves;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.smart.sorters.visitors.LinkMoveToHashMap;
import net.chesstango.search.visitors.SetGameVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static net.chesstango.search.Bound.EXACT;
import static net.chesstango.search.Bound.LOWER_BOUND;
import static net.chesstango.search.smart.Constants.DEFAULT_STALE_AGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class NodeMoveSorterInteriorTest {
    private final SimpleMoveEncoder simpleMoveEncoder = SimpleMoveEncoder.INSTANCE;

    private MoveSorter moveSorterInterior;

    private SearchListenerMediator searchListenerMediator;

    private TTable tTable;
    private TranspositionEntry transpositionEntry;

    private KillerMoves killerMoves;

    @BeforeEach
    public void setUp() {
        searchListenerMediator = new SearchListenerMediator();

        MoveSorterBuilder moveSorterBuilder = new MoveSorterInteriorBuilder()
                .withIterativeDeepening()
                .withGameEvaluatorCache()
                .withTranspositionTable()
                .withKillerMove()
                .withRecapture()
                .withMvvLva()
                .withSmartListenerMediator(searchListenerMediator);

        TranspositionTableBuilder transpositionTableBuilder = new TranspositionTableBuilder()
                .withHashSize(16)
                .withStaleAge(DEFAULT_STALE_AGE)
                .withSmartListenerMediator(searchListenerMediator);

        KillerMoveBuilder killerMoveBuilder = new KillerMoveBuilder()
                .withSmartListenerMediator(searchListenerMediator);

        EvaluationBuilder evaluationBuilder = new EvaluationBuilder()
                .withGameEvaluator(Evaluator.createInstance())
                .withGameEvaluatorCache()
                .withSmartListenerMediator(searchListenerMediator);

        moveSorterInterior = moveSorterBuilder.build();
        transpositionTableBuilder.build();
        killerMoveBuilder.build();
        evaluationBuilder.build();

        transpositionTableBuilder.link();
        killerMoveBuilder.link();
        evaluationBuilder.link();

        searchListenerMediator.accept(new LinkMoveToHashMap(new MoveToHashMap()));

        tTable = transpositionTableBuilder.getTTableImp();
        transpositionEntry = new TranspositionEntry();

        killerMoves = killerMoveBuilder.getKillerMovesTableImp();
    }

    @Test
    @Disabled
    public void test() {
        Game game = Game.from(FEN.START_POSITION);
        game.executeMove(Square.e2, Square.e4); // RecaptureMoveComparator requiere un movimiento anterior

        searchListenerMediator.accept(new SetGameVisitor(game));


        Iterable<Move> moves = moveSorterInterior.getOrderedMoves(1);
    }


    @Test
    public void test2() {
        Game game = Game.from(FEN.from("3k4/p2r4/1pR4p/4Q3/8/5P2/q5P1/6K1 w - - 0 1"))
                .executeMove(Square.e5, Square.g3);

        searchListenerMediator.accept(new SetGameVisitor(game));

        /**
         * Settup
         */
        ttWrite(0x13A63803694AEEE9L, (byte) 2, (short) 513, 1, LOWER_BOUND); // a2b1
        ttWrite(0x0AC497FAE0E135CEL, (byte) 0, (short) 1422, 7, EXACT);      // a2g2

        killerMoves.trackKillerMove(getMove(game, "a2a1"), 2);
        killerMoves.trackKillerMove(getMove(game, "a2d2"), 2);

        /**
         * Execute
         */

        List<String> actualSort = toMoveStrList(moveSorterInterior.getOrderedMoves(1));

        /**
         * Assertions
         */
        assertEquals(List.of("a2b1", "a2g2", "a2a1", "a2d2", "a2f2", "a2e2", "a2c2", "a2b2", "a2b3", "a2a3", "a2c4", "a2a4", "a2d5", "a2a5", "a2e6", "a2a6", "a2f7", "a2g8", "d7d1", "d7d2", "d7d3", "d7d4", "d7d5", "d7d6", "d7h7", "d7g7", "d7f7", "d7e7", "d7c7", "d7b7", "h6h5", "b6b5", "a7a5", "a7a6", "d8e7", "d8e8"), actualSort);
    }


    private void ttWrite(long hash, byte draft, short move, int value, Bound bound) {
        transpositionEntry.setHash(hash);
        transpositionEntry.setDraft(draft);
        transpositionEntry.setMove(move);
        transpositionEntry.setValue(value);
        transpositionEntry.setBound(bound);
        tTable.save(transpositionEntry);
    }

    private Move getMove(Game game, String moveStr) {
        for (Move move : game.getPossibleMoves()) {
            if (moveStr.equals(simpleMoveEncoder.encode(move))) {
                return move;
            }
        }
        throw new RuntimeException("No se encontro el movimiento " + moveStr);
    }

    private List<String> toMoveStrList(Iterable<Move> orderedMoves) {
        List<String> result = new ArrayList<>();
        for (Move move : orderedMoves) {
            result.add(simpleMoveEncoder.encode(move));
        }
        return result;
    }

}
