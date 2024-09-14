package net.chesstango.search.smart.features.pv.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetTrianglePV implements SearchByCycleListener, SearchByDepthListener {

    @Setter
    private Evaluator evaluator;

    private final short[][] trianglePV;
    private Game game;

    private List<PrincipalVariation> principalVariation;
    private boolean pvComplete;

    public SetTrianglePV() {
        trianglePV = new short[40][40];
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }

    @Override
    public void afterSearch(SearchResult result) {
        result.setPrincipalVariation(principalVariation);
        result.setPvComplete(pvComplete);
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        context.setTrianglePV(trianglePV);
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

        Move move = null;
        long hash = 0;
        int pvMoveCounter = 0;
        short[] pvMoves = trianglePV[0];
        do {
            move = readMove(pvMoves[pvMoveCounter]);
            hash = game.getChessPosition().getZobristHash();

            principalVariation.add(new PrincipalVariation(hash, move));

            game.executeMove(move);

            pvMoveCounter++;

        } while (pvMoves[pvMoveCounter] != 0);

        int pvEvaluation = evaluator.evaluate();

        // En caso que se llegó a loop
        if (game.getState().getRepetitionCounter() > 1) {
            pvEvaluation = 0;
        }

        if (bestMoveEvaluation.evaluation() == pvEvaluation) {
            pvComplete = true;
        } else {
            SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();
            throw new RuntimeException(String.format("bestEvaluation (%d) no coincide con la evaluacion PV (%d)", bestMoveEvaluation.evaluation(), pvEvaluation, principalVariation.stream().map(PrincipalVariation::move).toList()));
        }

        for (int i = 0; i < pvMoveCounter; i++) {
            game.undoMove();
        }
    }

    private Move readMove(short bestMoveEncoded) {
        for (Move posibleMove : game.getPossibleMoves()) {
            if (posibleMove.binaryEncoding() == bestMoveEncoded) {
                return posibleMove;
            }
        }
        throw new RuntimeException("Move not found");
    }
}
