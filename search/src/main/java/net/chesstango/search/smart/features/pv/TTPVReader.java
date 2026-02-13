package net.chesstango.search.smart.features.pv;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;
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
public class TTPVReader implements PVReader, Acceptor {

    @Setter
    private Evaluator evaluator;

    @Setter
    private TTable maxMap;

    @Setter
    private TTable minMap;

    @Setter
    private TTable qMaxMap;

    @Setter
    private TTable qMinMap;

    @Setter
    private Game game;

    @Setter
    private int maxPly;

    @Getter
    private boolean pvComplete;

    @Getter
    private List<PrincipalVariation> principalVariation;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void readPrincipalVariation(short bestMove, int bestValue) {
        principalVariation = new ArrayList<>();
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

    Move readMoveFromTT(TTable maxMap, TTable minMap) {
        Move result = null;
        if (maxMap != null && minMap != null) {
            long hash = game.getPosition().getZobristHash();
            TranspositionEntry entry = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? maxMap.read(hash) : minMap.read(hash);
            if (entry != null && TranspositionBound.EXACT.equals(entry.transpositionBound)) {
                short bestMoveEncoded = entry.move;
                result = getMove(bestMoveEncoded);
            }
        }
        return result;
    }

    Move getMove(short moveEncoded) {
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
