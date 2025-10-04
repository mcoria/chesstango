package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.search.smart.features.transposition.TranspositionBound;
import net.chesstango.search.visitors.SetGameVisitor;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class NodeSorter02Test extends AbstractNodeSorterTest {


    @Test
    public void kmDisabledTest() {
        loadTranspositionTables();

        moveSorterBuilder
                .withTranspositionTable()
                .withGameEvaluatorCache(loadEvaluationCache());

        MoveSorter moveSorter = moveSorterBuilder.build();

        SetGameVisitor gameVisitor = new SetGameVisitor(game);
        searchListenerMediator.getAcceptors().forEach(acceptor -> acceptor.accept(gameVisitor));
        searchListenerMediator.triggerBeforeSearch(cycleContext);
        searchListenerMediator.triggerBeforeSearchByDepth(depthContext);

        Iterable<Move> orderedMoves = moveSorter.getOrderedMoves(1);

        List<String> orderedMovesStr = convertMoveListToStringList(orderedMoves);

        assertEquals("[b8f8, a5c7, a5a8, a5a7, a5b6, a5a6, a5h5, a5g5, a5f5, a5e5, a5d5, a5c5, a5b5, a5b4, a5a4, a5c3, a5a3, a5d2, a5a2, a5e1, a5a1, g2h4, g2f4, g2e3, g2e1, b8e8, b8d8, b8c8, b8a8, b8b7, b8b6, b8b5, b8b4, b8b3, b8b2, b8b1, g4g5, h3h4, h3g3]",
                orderedMovesStr.toString());
    }

    @Test
    public void kmEnabledTest() {
        loadKillerMoveTables();
        loadTranspositionTables();

        moveSorterBuilder
                .withTranspositionTable()
                .withKillerMoveSorter()
                .withGameEvaluatorCache(loadEvaluationCache());

        MoveSorter moveSorter = moveSorterBuilder.build();

        SetGameVisitor gameVisitor = new SetGameVisitor(game);
        searchListenerMediator.getAcceptors().forEach(acceptor -> acceptor.accept(gameVisitor));
        searchListenerMediator.triggerBeforeSearch(cycleContext);
        searchListenerMediator.triggerBeforeSearchByDepth(depthContext);

        Iterable<Move> orderedMoves = moveSorter.getOrderedMoves(1);

        List<String> orderedMovesStr = convertMoveListToStringList(orderedMoves);

        assertEquals("[b8f8, a5c7, g2h4, a5a8, a5a7, a5b6, a5a6, a5h5, a5g5, a5f5, a5e5, a5d5, a5c5, a5b5, a5b4, a5a4, a5c3, a5a3, a5d2, a5a2, a5e1, a5a1, g2f4, g2e3, g2e1, b8e8, b8d8, b8c8, b8a8, b8b7, b8b6, b8b5, b8b4, b8b3, b8b2, b8b1, g4g5, h3h4, h3g3]",
                orderedMovesStr.toString());
    }

    @Override
    protected Game createGame() {
        return Game.from(FEN.of("1R3b1k/2p3pp/4qr2/Q7/3p2P1/3P3K/6NP/8 b - - 0 1"))
                .executeMove(Square.e6, Square.e2);
    }

    @Override
    protected int getMaxSearchPly() {
        return 2;
    }

    protected void loadTranspositionTables() {
        maxMap.write(0x337D4750B1C4CD1AL, 1, 4078092922146620L, TranspositionBound.LOWER_BOUND); // b8f8
    }

    protected EvaluatorCacheReadMock loadEvaluationCache() {
        Map<Long, Integer> cacheEvaluation = new HashMap<>();
        cacheEvaluation.put(0x8814FB171AC56D0BL, -26460); // a5c7

        return new EvaluatorCacheReadMock().setCache(cacheEvaluation);
    }

    private void loadKillerMoveTables() {
        //g2=KNIGHT_WHITE h4=null - MoveImp]
        killerMovesTable.trackKillerMove(createMove(PiecePositioned.of(Square.g2, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.h4), null, true), 2);
    }

}
