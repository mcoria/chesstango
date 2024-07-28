package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;
import net.chesstango.search.smart.sorters.MoveSorter;

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
public class AlphaBetaHypothesisValidator implements AlphaBetaFilter, SearchByCycleListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    protected Game game;

    protected Move expectedRootBestMove;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();

        Map<SearchParameter, Object> searchParameters = context.getSearchParameters();
        if (!searchParameters.containsKey(EPD_PARAMS)) {
            throw new RuntimeException("EPD_PARAMS not present in searchParameters");
        }

        EPD epd = (EPD) searchParameters.get(EPD_PARAMS);
        if (epd.getBestMoves() != null) {
            this.expectedRootBestMove = epd.getBestMoves().getFirst();
        } else if (epd.getSuppliedMove() != null) {
            this.expectedRootBestMove = epd.getSuppliedMove();
        } else {
            throw new RuntimeException("ExpectedRootBestMove not present in EPD entry");
        }
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        Move bestMove = expectedRootBestMove;
        int maxValue = exploreMove(next::minimize, currentPly, alpha, beta);

        Iterator<Move> moveIterator = game.getPossibleMoves().iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            if (!move.equals(expectedRootBestMove)) {
                game = game.executeMove(move);
                long bestMoveAndValue = next.minimize(currentPly + 1, maxValue, maxValue + 1);
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    bestMove = move;
                    search = false;
                }
                game = game.undoMove();
            }
        }
        return TranspositionEntry.encode(bestMove, maxValue);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        Move bestMove = expectedRootBestMove;
        int minValue = exploreMove(next::maximize, currentPly, alpha, beta);

        Iterator<Move> moveIterator = game.getPossibleMoves().iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            if (!move.equals(expectedRootBestMove)) {
                game = game.executeMove(move);
                long bestMoveAndValue = next.maximize(currentPly + 1, minValue - 1, minValue);
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
                if (currentValue < minValue) {
                    minValue = currentValue;
                    bestMove = move;
                    search = false;
                }
                game = game.undoMove();
            }
        }
        return TranspositionEntry.encode(bestMove, minValue);
    }


    protected int exploreMove(final AlphaBetaFunction alphaBetaFn, final int currentPly, final int alpha, final int beta) {
        game = game.executeMove(expectedRootBestMove);
        long bestMoveAndValue = alphaBetaFn.search(currentPly + 1, alpha, beta);
        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
        game = game.undoMove();
        return currentValue;
    }

}
