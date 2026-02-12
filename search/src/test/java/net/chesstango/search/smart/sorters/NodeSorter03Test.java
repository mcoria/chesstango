package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaHelper;
import net.chesstango.search.smart.features.transposition.TranspositionBound;
import net.chesstango.search.visitors.SetGameVisitor;
import net.chesstango.search.visitors.SetSearchMaxPlyVisitor;
import net.chesstango.search.visitors.SetTTableVisitor;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class NodeSorter03Test extends AbstractNodeSorterTest {


    @Test
    public void qSearchTakenIntoAccountTest() {
        loadTranspositionTables();
        loadKillerMoveTables();

        moveSorterBuilder
                .withTranspositionTable()
                .withGameEvaluatorCache(loadEvaluationCache());

        MoveSorter moveSorter = moveSorterBuilder.build();

        searchListenerMediator.accept(new SetGameVisitor(game));
        searchListenerMediator.accept(new SetTTableVisitor(maxMap, minMap, qMaxMap, qMinMap));
        searchListenerMediator.triggerBeforeSearch();

        searchListenerMediator.accept(new SetSearchMaxPlyVisitor(3));
        searchListenerMediator.triggerBeforeSearchByDepth();

        Iterable<Move> orderedMoves = moveSorter.getOrderedMoves(2);

        List<String> orderedMovesStr = convertMoveListToStringList(orderedMoves);

        assertEquals("[h5e2, h5h3, h5g5, h5h4, h5f3, h5g6, d1d5, c2e4, f5g7, h5h7, h5h6, h5g4, f5e7, f5h6, f5d6, f5h4, f5d4, f5g3, e3a7, e3h6, e3b6, e3g5, e3c5, e3f4, e3d4, e3d2, e3c1, c2a4, c2d3, c2b3, c2b1, d1d4, d1d3, d1d2, d1f1, d1e1, d1c1, d1b1, a1a8, a1a7, a1a6, a1a5, a1a4, a1a3, a1a2, a1c1, a1b1, h2h4, h2h3, g2g4, g2g3, f2f4, f2f3, b2b4, b2b3, g1h1, g1f1]",
                orderedMovesStr.toString());
    }

    @Override
    protected Game createGame() {
        return Game.from(FEN.of("3b1rk1/1bq3pp/5pn1/1p2rN2/2p1p3/2P1B2Q/1PB2PPP/R2R2K1 w - - 0 1"))
                .executeMove(Square.h3, Square.h5)
                .executeMove(Square.b7, Square.d5);
    }

    protected void loadTranspositionTables() {
        minMap.write(0xC23A9796AF1A652FL, TranspositionBound.UPPER_BOUND, 0, AlphaBetaHelper.decodeMove(4294896611L), AlphaBetaHelper.decodeValue(4294896611L)); // h5e2
        minMap.write(0x1154F9546860B223L, TranspositionBound.UPPER_BOUND, 0, AlphaBetaHelper.decodeMove(4294894301L), AlphaBetaHelper.decodeValue(4294894301L)); // h5h3
        minMap.write(0x7A3B2C64C1AB8AB1L, TranspositionBound.UPPER_BOUND, 0, AlphaBetaHelper.decodeMove(4294889071L), AlphaBetaHelper.decodeValue(4294889071L)); // h5g5
        minMap.write(0xD8727DF238E25CA3L, TranspositionBound.UPPER_BOUND, 0, AlphaBetaHelper.decodeMove(4294886221L), AlphaBetaHelper.decodeValue(4294886221L)); // h5h4
        minMap.write(0xEE1E6387CD236810L, TranspositionBound.UPPER_BOUND, 0, AlphaBetaHelper.decodeMove(4294886201L), AlphaBetaHelper.decodeValue(4294886201L)); // h5f3
        minMap.write(0xE38DB33F8781BF41L, TranspositionBound.UPPER_BOUND, 0, AlphaBetaHelper.decodeMove(3920862759355397L), AlphaBetaHelper.decodeValue(3920862759355397L)); // h5g6

        qMinMap.write(0x5809B10F4AEFE9B8L, TranspositionBound.UPPER_BOUND, 1, AlphaBetaHelper.decodeMove(2571761992221855L), AlphaBetaHelper.decodeValue(2571761992221855L)); // d1d5
        qMinMap.write(0xEE7E1C867AC579F6L, TranspositionBound.UPPER_BOUND, 1, AlphaBetaHelper.decodeMove(2493696666611559L), AlphaBetaHelper.decodeValue(2493696666611559L)); // c2e4
        qMinMap.write(0xE5878EF506AAEC8AL, TranspositionBound.UPPER_BOUND, 1, AlphaBetaHelper.decodeMove(4422240061729033L), AlphaBetaHelper.decodeValue(4422240061729033L)); // f5g7
        qMinMap.write(0x591BAC29C5110DE5L, TranspositionBound.UPPER_BOUND, 1, AlphaBetaHelper.decodeMove(4423339573088489L), AlphaBetaHelper.decodeValue(4423339573088489L)); // h5h7

        maxMap.write(0x0CE7DD3862149D3EL, TranspositionBound.UPPER_BOUND, 0, AlphaBetaHelper.decodeMove(4294899481L), AlphaBetaHelper.decodeValue(4294899481L)); // NO_MOVE
        qMaxMap.write(0x0CE7DD3862149D3EL, TranspositionBound.UPPER_BOUND, 0, AlphaBetaHelper.decodeMove(4294899481L), AlphaBetaHelper.decodeValue(4294899481L)); // NO_MOVE
    }

    protected EvaluatorCacheReadMock loadEvaluationCache() {
        Map<Long, Integer> cacheEvaluation = new HashMap<>();
        return new EvaluatorCacheReadMock().setCache(cacheEvaluation);
    }

    private void loadKillerMoveTables() {
        //factory[g4=QUEEN_WHITE g3=null - MoveImp]
        killerMovesTable.trackKillerMove(createMove(PiecePositioned.of(Square.g4, Piece.QUEEN_WHITE), PiecePositioned.getPosition(Square.g3), Cardinal.Sur, true), 3);
    }
}
