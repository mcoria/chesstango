package net.chesstango.search.smart.features.pv.listeners;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetTranspositionPV implements SearchByCycleListener, SearchByDepthListener {

    @Setter
    private GameEvaluator gameEvaluator;
    private TTable maxMap;
    private TTable minMap;
    private TTable qMaxMap;
    private TTable qMinMap;
    private Game game;

    private int maxPly;
    private List<Move> principalVariation;
    private boolean pvComplete;


    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
        this.qMaxMap = context.getQMaxMap();
        this.qMinMap = context.getQMinMap();
        this.principalVariation = null;
    }

    @Override
    public void afterSearch(SearchMoveResult searchMoveResult) {
        searchMoveResult.setPrincipalVariation(principalVariation);
        searchMoveResult.setPvComplete(pvComplete);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
    }

    @Override
    public void afterSearchByDepth(SearchByDepthResult searchByDepthResult) {
        calculatePrincipalVariation(searchByDepthResult.getBestMoveEvaluation());
        searchByDepthResult.setPrincipalVariation(principalVariation);
        searchByDepthResult.setPvComplete(pvComplete);
    }


    protected void calculatePrincipalVariation(MoveEvaluation bestMoveEvaluation) {
        principalVariation = new ArrayList<>();
        pvComplete = false;

        Move move = bestMoveEvaluation.move();
        int pvMoveCounter = 0;
        do {

            principalVariation.add(move);

            game.executeMove(move);

            pvMoveCounter++;

            move = principalVariation.size() < maxPly
                    ? readMoveFromTT(maxMap, minMap)
                    : readMoveFromTT(qMaxMap, qMinMap);

        } while (move != null);

        int pvEvaluation = gameEvaluator.evaluate();

        // En caso que se llegó a loop
        if (game.getState().getRepetitionCounter() > 1) {
            pvEvaluation = 0;
        }

        if (bestMoveEvaluation.evaluation() == pvEvaluation) {
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
            if (entry != null) {
                short bestMoveEncoded = TranspositionEntry.decodeBestMove(entry.movesAndValue);
                for (Move posibleMove : game.getPossibleMoves()) {
                    if (posibleMove.binaryEncoding() == bestMoveEncoded) {
                        result = posibleMove;
                        break;
                    }
                }
            }
        }

        return result;
    }
}
