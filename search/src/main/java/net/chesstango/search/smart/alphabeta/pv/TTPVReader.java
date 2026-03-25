package net.chesstango.search.smart.alphabeta.pv;

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
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionBound;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * TTPVReader will not be considering for statistics purposes.
 *
 * @author Mauricio Coria
 */
public class TTPVReader implements PVReader, SearchByCycleListener, SearchByDepthListener {

    @Setter
    private Evaluator evaluator;

    @Setter
    private TTable maxMap;

    @Setter
    private TTable minMap;

    @Setter
    private Game game;

    @Setter
    private int depth;

    @Getter
    boolean pvComplete;

    @Getter
    List<PrincipalVariation> principalVariation;

    private final TranspositionEntry entryWorkspace;

    public TTPVReader() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        // Solo al comienzo de la busqueda para limpiar la lista de PVs
        principalVariation = new ArrayList<>();
    }

    @Override
    public void beforeSearchByDepth() {
        // Cada vez que profundizamos comenzamos la lista de movimientos no se encuentra completa
        pvComplete = false;
    }

    @Override
    public void readPrincipalVariation(short bestMove, int bestValue) {
        // Cada vez que recalculamos Principal Variation
        principalVariation.clear();
        pvComplete = false;

        // First PV move
        final long previousHash = game.getHistory().peekLastRecord().zobristHash().getZobristHash();
        final Move previousMove = game.getHistory().peekLastRecord().playedMove();
        principalVariation.add(new PrincipalVariation(previousHash, previousMove));


        // Second PV move
        Deque<Move> moves = new LinkedList<>();
        long currentHash = game.getPosition().getZobristHash();
        Move currentMove = getMove(bestMove);

        while (currentMove != null) {

            principalVariation.add(new PrincipalVariation(currentHash, currentMove));

            currentMove.executeMove();

            moves.push(currentMove);

            // Third PV move and onward
            currentHash = game.getPosition().getZobristHash();
            currentMove = readMoveFromTT();
        }

        int pvEvaluation = evaluator.evaluate();

        // En caso que se llegó a loop
        if (game.getState().getRepetitionCounter() > 1) {
            pvEvaluation = 0;
        }

        if (bestValue == pvEvaluation && principalVariation.size() >= depth) {
            pvComplete = true;
        }

        while (!moves.isEmpty()) {
            Move move = moves.pop();
            move.undoMove();
        }
    }

    final Move readMoveFromTT() {
        Move result = null;
        if (maxMap != null && minMap != null) {
            long hash = game.getPosition().getZobristHash();
            boolean load = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? maxMap.load(hash, entryWorkspace) : minMap.load(hash, entryWorkspace);
            if (load && TranspositionBound.EXACT.equals(entryWorkspace.getBound())) {
                short bestMoveEncoded = entryWorkspace.getMove();
                result = bestMoveEncoded != 0 ? getMove(bestMoveEncoded) : null;
            }
        }
        return result;
    }

    final Move getMove(short moveEncoded) {
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
