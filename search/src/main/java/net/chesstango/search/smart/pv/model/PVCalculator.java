package net.chesstango.search.smart.pv.model;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.PVMove;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.egtb.EndGameTableBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PVCalculator implements Acceptor {

    @Setter
    protected Evaluator evaluator;

    @Setter
    protected EndGameTableBase endGameTableBase;

    @Setter
    protected Game game;

    @Setter
    private PVTable trianglePV;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    public PrincipalVariation calculatePrincipalVariation(int eval) {
        // Cada vez que recalculamos Principal Variation
        List<PVMove> pvMoves = walkPrincipalVariation();

        boolean pvComplete = validatePrincipalVariation(pvMoves, eval);

        // Rewind game
        pvMoves
                .stream()
                .skip(1)
                .map(PVMove::move)
                .toList()
                .reversed()
                .forEach(Move::undoMove);


        return new PrincipalVariation(pvMoves, pvComplete);
    }


    protected List<PVMove> walkPrincipalVariation() {
        // Comenzar de ROOT + 2
        int pvMoveCounter = 2;

        Move[] pvMoves = trianglePV.getRootPV();
        List<PVMove> pvMoveList = new ArrayList<>(pvMoves.length);

        // First PV move
        Move lastMove = game.getHistory().peekLastRecord().playedMove();
        long lastHash = game.getHistory().peekLastRecord().zobristHash().getZobristHash();
        pvMoveList.add(new PVMove(lastHash, lastMove));

        // Second PV move
        while (pvMoveCounter < pvMoves.length) {
            long currentHash = game.getPosition().getZobristHash();

            Move currentMove = pvMoves[pvMoveCounter++];

            pvMoveList.add(new PVMove(currentHash, currentMove));

            currentMove.executeMove();
        }

        return pvMoveList;
    }

    protected boolean validatePrincipalVariation(List<PVMove> pvMoves, int eval) {
        boolean isPVComplete = false;

        int sign = pvMoves.size() % 2 == 0 ? 1 : -1;

        int pvEvaluation = 0;

        // En caso que se llegó a loop
        if (game.getState().getRepetitionCounter() > 1) {
            // Nothing to do
        } else if (endGameTableBase.isProbeAvailable()) {
            pvEvaluation = sign * (Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? endGameTableBase.evaluate() : -endGameTableBase.evaluate());
        } else {
            pvEvaluation = sign * (Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? evaluator.evaluate() : -evaluator.evaluate());
        }

        // No se debe considerar DEPH dado que al entrar en LOOP la cantidad de movimientos PVs puede ser menor a DEPTH
        if (eval == pvEvaluation) {
            isPVComplete = true;
        }

        return isPVComplete;
    }

}
