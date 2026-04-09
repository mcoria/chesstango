package net.chesstango.search.smart.alphabeta.pv;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static net.chesstango.search.Bound.EXACT;

/**
 * TTPVReader will not be considering for statistics purposes.
 *
 * @author Mauricio Coria
 */
public class PVCalculatorTransposition implements PVCalculator, SearchByCycleListener, SearchByDepthListener {

    @Setter
    private Evaluator evaluator;

    @Setter
    private EndGameTableBase endGameTableBase;

    @Setter
    private TTable maxMap;

    @Setter
    private TTable minMap;

    @Setter
    private Game game;

    @Setter
    private int depth;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private boolean pvComplete;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private List<PrincipalVariation> principalVariation;

    private final TranspositionEntry entryWorkspace;

    public PVCalculatorTransposition() {
        entryWorkspace = new TranspositionEntry();
    }

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

    @Override
    public void calculatePrincipalVariation(short bestMove, int bestValue) {
        // Cada vez que recalculamos Principal Variation
        principalVariation = new ArrayList<>();
        pvComplete = false;

        // First PV move
        final long previousHash = game.getHistory().peekLastRecord().zobristHash().getZobristHash();
        final Move previousMove = game.getHistory().peekLastRecord().playedMove();
        principalVariation.add(new PrincipalVariation(previousHash, previousMove));


        Deque<Move> moves = new LinkedList<>();

        if (bestMove != 0) {
            // Second PV move
            long currentHash = game.getPosition().getZobristHash();
            Move currentMove = getMove(bestMove);

            while (currentMove != null) {

                principalVariation.add(new PrincipalVariation(currentHash, currentMove));

                currentMove.executeMove();

                moves.push(currentMove);

                // Third PV move and onward
                currentHash = game.getPosition().getZobristHash();
                currentMove = readMoveFromTT(currentHash);
            }
        }

        int pvEvaluation = evaluator.evaluate();

        // En caso que se llegó a loop
        if (game.getState().getRepetitionCounter() > 1) {
            pvEvaluation = 0;
        } else if (endGameTableBase.isProbeAvailable()) {
            pvEvaluation = endGameTableBase.evaluate();
        }

        if (bestValue == pvEvaluation && principalVariation.size() >= depth) {
            pvComplete = true;
        }

        while (!moves.isEmpty()) {
            Move move = moves.pop();
            move.undoMove();
        }
    }

    final Move readMoveFromTT(long hash) {
        Move result = null;
        if (maxMap != null && minMap != null) {
            boolean load = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? maxMap.load(hash, entryWorkspace) : minMap.load(hash, entryWorkspace);
            if (load && EXACT.equals(entryWorkspace.getBound())) {
                short bestMoveEncoded = entryWorkspace.getMove();
                result = bestMoveEncoded != 0 ? getMove(bestMoveEncoded) : null;
            }
        }
        return result;
    }

    final Move getMove(short moveEncoded) {
        for (Move posibleMove : game.getPossibleMoves()) {
            if (posibleMove.binaryEncoding() == moveEncoded) {
                return posibleMove;
            }
        }
        return null;
    }
}
