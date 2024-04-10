package net.chesstango.search.smart.features.pv.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionBound;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionPV implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {


    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    private GameEvaluator gameEvaluator;

    private List<Move> principalVariation;
    private boolean pvComplete;

    private TTable maxMap;
    private TTable minMap;
    private TTable qMaxMap;
    private TTable qMinMap;
    private Game game;
    private int maxPly;


    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
        this.qMaxMap = context.getQMaxMap();
        this.qMinMap = context.getQMinMap();
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        result.setPrincipalVariation(principalVariation);
        result.setPvComplete(pvComplete);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
    }

    @Override
    public void afterSearchByDepth(SearchByDepthResult result) {
        result.setPrincipalVariation(principalVariation);
        result.setPvComplete(pvComplete);
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long moveAndValue = next.maximize(currentPly, alpha, beta);
        int currentValue = TranspositionEntry.decodeValue(moveAndValue);

        if (currentValue < beta) {
            calculatePrincipalVariation(moveAndValue);
        }

        return moveAndValue;
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long moveAndValue = next.minimize(currentPly, alpha, beta);
        int currentValue = TranspositionEntry.decodeValue(moveAndValue);
        if (alpha < currentValue) {
            calculatePrincipalVariation(moveAndValue);
        }
        return moveAndValue;
    }


    protected void calculatePrincipalVariation(long moveAndValue) {
        int pvMoveCounter = 0;
        principalVariation = new ArrayList<>();
        pvComplete = false;

        Move currentMove = game.getState().getPreviousState().getSelectedMove();
        principalVariation.add(currentMove);

        final int bestValue = TranspositionEntry.decodeValue(moveAndValue);
        final short bestMoveEncoded = TranspositionEntry.decodeBestMove(moveAndValue);


        currentMove = getMove(bestMoveEncoded);
        while (currentMove != null) {

            principalVariation.add(currentMove);

            game.executeMove(currentMove);

            pvMoveCounter++;

            currentMove = principalVariation.size() < maxPly
                    ? readMoveFromTT(maxMap, minMap)
                    : readMoveFromTT(qMaxMap, qMinMap);

        }

        int pvEvaluation = gameEvaluator.evaluate();

        // En caso que se llegó a loop
        if (game.getState().getRepetitionCounter() > 1) {
            pvEvaluation = 0;
        }

        if (bestValue == pvEvaluation) {
            pvComplete = true;
        }

        for (int i = 0; i < pvMoveCounter; i++) {
            game.undoMove();
        }
    }

    private Move readMoveFromTT(TTable maxMap, TTable minMap) {
        Move result = null;
        if (maxMap != null && minMap != null) {
            long hash = game.getChessPosition().getZobristHash();
            TranspositionEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.read(hash) : minMap.read(hash);
            if (entry != null && TranspositionBound.EXACT.equals(entry.transpositionBound)) {
                short bestMoveEncoded = TranspositionEntry.decodeBestMove(entry.movesAndValue);
                result = getMove(bestMoveEncoded);
            }
        }
        return result;
    }

    private Move getMove(short moveEncoded) {
        Move result = null;
        for (Move posibleMove : game.getPossibleMoves()) {
            if (posibleMove.binaryEncoding() == moveEncoded) {
                result = posibleMove;
                break;
            }
        }
        return result;
    }
}
