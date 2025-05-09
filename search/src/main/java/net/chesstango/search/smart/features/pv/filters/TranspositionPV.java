package net.chesstango.search.smart.features.pv.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionBound;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionPV implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {


    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    private Evaluator evaluator;
    private List<PrincipalVariation> principalVariation;
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
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth result) {
        result.setPrincipalVariation(principalVariation);
        result.setPvComplete(pvComplete);
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long moveAndValue = next.maximize(currentPly, alpha, beta);

        return process(alpha, beta, moveAndValue);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long moveAndValue = next.minimize(currentPly, alpha, beta);

        return process(alpha, beta, moveAndValue);
    }


    private long process(int alpha, int beta, long moveAndValue) {
        int currentValue = TranspositionEntry.decodeValue(moveAndValue);

        if (alpha < currentValue && currentValue < beta) {
            calculatePrincipalVariation(moveAndValue);
        }

        return moveAndValue;
    }


    protected void calculatePrincipalVariation(long moveAndValue) {
        Deque<Move> moves = new LinkedList<>();
        principalVariation = new ArrayList<>();
        pvComplete = false;

        final long lastHash = game.getHistory().peekLastRecord().zobristHash().getZobristHash();
        final Move lastMove = game.getHistory().peekLastRecord().playedMove();
        principalVariation.add(new PrincipalVariation(lastHash, lastMove));

        final int bestValue = TranspositionEntry.decodeValue(moveAndValue);
        final short bestMoveEncoded = TranspositionEntry.decodeBestMove(moveAndValue);


        long currentHash = game.getPosition().getZobristHash();
        Move currentMove = getMove(bestMoveEncoded);
        while (currentMove != null) {

            principalVariation.add(new PrincipalVariation(currentHash, currentMove));

            currentMove.executeMove();

            moves.push(currentMove);

            currentHash = game.getPosition().getZobristHash();
            currentMove = principalVariation.size() < maxPly
                    ? readMoveFromTT(maxMap, minMap)
                    : readMoveFromTT(qMaxMap, qMinMap);

        }

        int pvEvaluation = evaluator.evaluate();

        // En caso que se llegó a loop
        if (game.getState().getRepetitionCounter() > 1) {
            pvEvaluation = 0;
        }

        if (bestValue == pvEvaluation) {
            pvComplete = true;
        }

        while (!moves.isEmpty()) {
            Move move = moves.pop();
            move.undoMove();
        }
    }

    private Move readMoveFromTT(TTable maxMap, TTable minMap) {
        Move result = null;
        if (maxMap != null && minMap != null) {
            long hash = game.getPosition().getZobristHash();
            TranspositionEntry entry = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? maxMap.read(hash) : minMap.read(hash);
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
