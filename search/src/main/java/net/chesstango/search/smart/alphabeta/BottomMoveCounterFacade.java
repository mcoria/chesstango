package net.chesstango.search.smart.alphabeta;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.GameMoveDecoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.epd.EPD;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;


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

    // Hace falta settear con un visitor
    private EPD epd;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();

        GameMoveDecoder moveDecoder = new GameMoveDecoder();
        if (epd.getBestMovesStr() != null) {
            String[] bestMoves = epd.getBestMovesStr().split(" ");
            String bestMoveStr = bestMoves[0];
            this.targetMove = moveDecoder.decode(bestMoveStr, game.getCurrentFEN());
        } else if (epd.getSuppliedMoveStr() != null) {
            this.targetMove = moveDecoder.decode(epd.getSuppliedMoveStr(), game.getCurrentFEN());
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
        final Color currentTurn = game.getPosition().getCurrentTurn();
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
        for (Move move : game.getPossibleMoves()) {
            if (!move.equals(targetMove)) {
                move.executeMove();
                long bestMoveAndValue = alphaBetaFilter.minimize(1, maxValue - 1, maxValue);
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
                if (currentValue < maxValue) {
                    this.bottomMoveCounter++;
                }
                move.undoMove();
            }
        }
    }

    protected void minimize(final int minValue) {
        for (Move move : game.getPossibleMoves()) {
            if (!move.equals(targetMove)) {
                move.executeMove();
                long bestMoveAndValue = alphaBetaFilter.maximize(1, minValue, minValue + 1);
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
                if (currentValue > minValue) {
                    this.bottomMoveCounter++;
                }
                move.undoMove();
            }
        }
    }
}
