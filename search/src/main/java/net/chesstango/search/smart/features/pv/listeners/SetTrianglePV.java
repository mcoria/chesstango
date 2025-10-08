package net.chesstango.search.smart.features.pv.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.*;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetTrianglePV implements SearchByCycleListener, SearchByDepthListener, Acceptor {

    @Setter
    private Evaluator evaluator;

    private final short[][] trianglePV;

    @Setter
    private Game game;

    private List<PrincipalVariation> principalVariation;
    private boolean pvComplete;

    public SetTrianglePV() {
        trianglePV = new short[40][40];
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        context.setTrianglePV(trianglePV);
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth searchResultByDepth) {
        calculatePrincipalVariation(searchResultByDepth.getBestMoveEvaluation());
        searchResultByDepth.setPrincipalVariation(principalVariation);
        searchResultByDepth.setPvComplete(pvComplete);
    }

    protected void calculatePrincipalVariation(MoveEvaluation bestMoveEvaluation) {
        principalVariation = new ArrayList<>();
        pvComplete = false;

        Move move = null;
        long hash = 0;
        int pvMoveCounter = 0;
        Deque<Move> moves = new LinkedList<>();
        short[] pvMoves = trianglePV[0];

        do {
            move = readMove(pvMoves[pvMoveCounter]);
            hash = game.getPosition().getZobristHash();

            principalVariation.add(new PrincipalVariation(hash, move));

            move.executeMove();

            moves.push(move);

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

        while (!moves.isEmpty()) {
            move = moves.pop();
            move.undoMove();
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
