package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.statistics.GameStatistics;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class DebugTree implements AlphaBetaFilter, SearchByCycleListener {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private PrintStream debugOut;

    private GameStatistics game;

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = (GameStatistics) context.getGame();
        this.debugOut = context.getDebugOut();
    }

    @Override
    public void afterSearch() {

    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return debugSearch(currentPly, alpha, beta, next::maximize, "MAX");
    }


    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return debugSearch(currentPly, alpha, beta, next::minimize, "MIN");
    }

    private long debugSearch(int currentPly, int alpha, int beta, AlphaBetaFunction fn, String fnString) {
        debugOut.print("\n");

        Move currentMove = game.getState().getPreviousState().getSelectedMove();

        debugOut.printf("%s%s %s alpha=%d beta=%d", ">\t".repeat(currentPly), simpleMoveEncoder.encode(currentMove), fnString, alpha, beta);

        int execMovesBeforeSearch = game.getExecutedMoves();

        long result = fn.search(currentPly, alpha, beta);

        int currentValue = TranspositionEntry.decodeValue(result);

        if (execMovesBeforeSearch < game.getExecutedMoves()) {
            debugOut.print("\n");
            debugOut.printf("%s%s %s value=%d", ">\t".repeat(currentPly), simpleMoveEncoder.encode(currentMove), fnString, currentValue);
        } else {
            debugOut.printf(" value=%d", currentValue);
        }

        return result;
    }
}
