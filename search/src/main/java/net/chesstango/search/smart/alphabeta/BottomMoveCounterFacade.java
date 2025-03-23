package net.chesstango.search.smart.alphabeta;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.move.MoveDecoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

import java.util.Iterator;
import java.util.Map;

import static net.chesstango.search.SearchParameter.EPD_PARAMS;

/**
 * Valida una hipotesis: que expectedRootBestMove es el mejor movimiento posible.
 * Al comienzo se realiza la busqueda de valor para expectedRootBestMove
 * y luego se explora los otros movimientos posibles con una ventana reducida,
 * es decir una busqueda de tipo PV con ventana null.
 * Tan pronto encuentra un movimiento superador, retorna.
 *
 * @author Mauricio Coria
 */
public class BottomMoveCounterFacade implements SearchAlgorithm {

    @Setter
    @Getter
    private AlphaBetaFilter alphaBetaFilter;

    private Game game;

    private Move targetMove;

    private int bottomMoveCounter;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();

        Map<SearchParameter, Object> searchParameters = context.getSearchParameters();
        if (!searchParameters.containsKey(EPD_PARAMS)) {
            throw new RuntimeException("EPD_PARAMS not present in searchParameters");
        }

        EPD epd = (EPD) searchParameters.get(EPD_PARAMS);
        MoveDecoder moveDecoder = new MoveDecoder();
        if (epd.getBestMovesStr() != null) {
            String[] bestMoves = epd.getBestMovesStr().split(" ");
            String bestMoveStr = bestMoves[0];
            this.targetMove = moveDecoder.decode(bestMoveStr, game.getPossibleMoves());
        } else if (epd.getSuppliedMoveStr() != null) {
            this.targetMove = moveDecoder.decode(epd.getSuppliedMoveStr(), game.getPossibleMoves());
        }

        if (this.targetMove == null) {
            throw new RuntimeException("ExpectedRootBestMove not present in EPD entry");
        }

        this.bottomMoveCounter = 0;
    }

    @Override
    public void afterSearch(SearchResult result) {
        result.setBottomMoveCounter(bottomMoveCounter);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
    }

    @Override
    public void search() {
        final Color currentTurn = game.getChessPosition().getCurrentTurn();
        if (Color.WHITE.equals(currentTurn)) {
            maximize(targetMoveEvaluation(alphaBetaFilter::minimize));
        } else {
            minimize(targetMoveEvaluation(alphaBetaFilter::maximize));
        }
    }

    protected int targetMoveEvaluation(final AlphaBetaFunction alphaBetaFn) {
        targetMove.executeMove();
        long bestMoveAndValue = alphaBetaFn.search(1, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE);
        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
        targetMove.undoMove();
        return currentValue;
    }

    protected void maximize(final int maxValue) {
        Iterator<Move> moveIterator = game.getPossibleMoves().iterator();
        while (moveIterator.hasNext()) {
            Move move = moveIterator.next();
            if (!move.equals(targetMove)) {
                targetMove.executeMove();
                long bestMoveAndValue = alphaBetaFilter.minimize(1, maxValue - 1, maxValue);
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
                if (currentValue < maxValue) {
                    this.bottomMoveCounter++;
                }
                targetMove.undoMove();
            }
        }
    }

    protected void minimize(final int minValue) {
        Iterator<Move> moveIterator = game.getPossibleMoves().iterator();
        while (moveIterator.hasNext()) {
            Move move = moveIterator.next();
            if (!move.equals(targetMove)) {
                targetMove.executeMove();
                long bestMoveAndValue = alphaBetaFilter.maximize(1, minValue, minValue + 1);
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
                if (currentValue > minValue) {
                    this.bottomMoveCounter++;
                }
                targetMove.undoMove();
            }
        }
    }
}
