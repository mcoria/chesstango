package net.chesstango.search.smart.evaluator;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.EvaluatorCacheRead;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugCacheRead;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.DebugReadTT;

import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class EvaluatorCacheDebug implements EvaluatorCacheRead, Acceptor {

    private DebugNodeTracker debugNodeTracker;

    private EvaluatorCacheRead evaluatorCacheRead;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Integer readFromCache(long hash) {
        Integer evaluation = evaluatorCacheRead.readFromCache(hash);
        if (evaluation != null) {
            trackReadFromCache(hash, evaluation);
        }
        return evaluation;
    }


    public void trackReadFromCache(long hashRequested, int evaluation) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();

        List<DebugCacheRead> evalCacheReads = currentNode.getEvalCacheReads();

        Optional<DebugCacheRead> previousReadOpt = evalCacheReads
                .stream()
                .filter(debugOperationEval -> debugOperationEval.getHashRequested() == hashRequested)
                .findFirst();

        if (previousReadOpt.isEmpty()) {
            currentNode.getEvalCacheReads().add(new DebugCacheRead()
                    .setHashRequested(hashRequested)
                    .setEvaluation(evaluation)
                    .setMove(readMove(hashRequested))
            );
        }
    }

    String readMove(long hashRequested) {
        for (Move move : game.getPossibleMoves()) {
            if (move.getZobristHash() == hashRequested) {
                return move.coordinateEncoding();
            }
        }
        return DebugReadTT.UNKNOWN;
    }
}
