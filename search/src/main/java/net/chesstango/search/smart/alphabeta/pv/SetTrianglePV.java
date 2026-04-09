package net.chesstango.search.smart.alphabeta.pv;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetTrianglePV implements SearchByCycleListener, SearchByDepthListener {

    @Setter
    private short[][] trianglePV;

    @Setter
    private Evaluator evaluator;

    @Setter
    private EndGameTableBase endGameTableBase;

    @Setter
    private Game game;

    @Getter
    private List<PrincipalVariation> principalVariation;

    @Getter
    private boolean pvComplete;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        principalVariation = null;
        pvComplete = false;
    }

    @Override
    public void beforeSearchByDepth() {
        principalVariation = null;
        pvComplete = false;
    }

    public void calculatePrincipalVariation(RootMoveEvaluation bestMoveEvaluation) {
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
        }else if (endGameTableBase.isProbeAvailable()) {
            pvEvaluation = endGameTableBase.evaluate();
        }

        if (bestMoveEvaluation.evaluation() == pvEvaluation) {
            pvComplete = true;
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
