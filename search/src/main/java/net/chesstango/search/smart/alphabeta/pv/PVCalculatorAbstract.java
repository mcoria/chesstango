package net.chesstango.search.smart.alphabeta.pv;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class PVCalculatorAbstract implements PVCalculator, SearchByCycleListener {

    @Setter
    protected Evaluator evaluator;

    @Setter
    protected EndGameTableBase endGameTableBase;

    @Setter
    protected Game game;

    @Setter
    protected int depth;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    protected boolean pvComplete;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    protected List<PrincipalVariation> principalVariation;

    @Override
    public void beforeSearch() {
        principalVariation = null;
        pvComplete = false;
    }

    protected abstract List<PrincipalVariation> walkPrincipalVariation(List<PrincipalVariation> principalVariationList, int eval);

    @Override
    public void calculatePrincipalVariation(int eval) {
        List<PrincipalVariation> principalVariationList = new ArrayList<>();

        // First PV move
        final long rootHash = game.getHistory().peekLastRecord().zobristHash().getZobristHash();
        final Move rootMove = game.getHistory().peekLastRecord().playedMove();
        principalVariationList.add(new PrincipalVariation(rootHash, rootMove));

        // Cada vez que recalculamos Principal Variation
        this.principalVariation = walkPrincipalVariation(principalVariationList, eval);
        this.pvComplete = validatePrincipalVariation(eval);

        // Rewind game
        for (int i = principalVariationList.size() - 1; i > 0; i--) {
            Move move = principalVariationList.get(i).move();
            move.undoMove();
        }
    }


    protected boolean validatePrincipalVariation(int eval) {
        boolean isPVComplete = false;

        int pvEvaluation = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? -evaluator.evaluate() : evaluator.evaluate();

        // En caso que se llegó a loop
        if (game.getState().getRepetitionCounter() > 1) {
            pvEvaluation = 0;
        } else if (endGameTableBase.isProbeAvailable()) {
            pvEvaluation = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? -endGameTableBase.evaluate() : endGameTableBase.evaluate();
        }

        if (eval == pvEvaluation && principalVariation.size() >= depth) {
            isPVComplete = true;
        }

        return isPVComplete;
    }

    protected Move getMove(short moveEncoded) {
        for (Move posibleMove : game.getPossibleMoves()) {
            if (posibleMove.binaryEncoding() == moveEncoded) {
                return posibleMove;
            }
        }
        return null;
    }
}
