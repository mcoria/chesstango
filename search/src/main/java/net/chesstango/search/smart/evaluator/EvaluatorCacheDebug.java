package net.chesstango.search.smart.evaluator;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.EvaluatorCacheRead;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.DebugCacheRead;

import java.util.Optional;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class EvaluatorCacheDebug implements EvaluatorCacheRead, Acceptor {

    private DebugNodeTracker debugNodeTracker;

    private EvaluatorCacheRead evaluatorCacheRead;

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


    public void trackReadFromCache(long hash, int evaluation) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();
        if (currentNode != null) {
            Optional<DebugCacheRead> previousReadOpt = currentNode
                    .getEvalCacheReads()
                    .stream()
                    .filter(debugOperationEval -> debugOperationEval.getHashRequested() == hash)
                    .findFirst();

            if (previousReadOpt.isPresent()) {
                DebugCacheRead previousReadOpEval = previousReadOpt.get();
                if (previousReadOpEval.getEvaluation() != evaluation) {
                    throw new RuntimeException("Lectura repetida pero distinto valor retornado");
                }
            } else {
                currentNode.getEvalCacheReads().add(new DebugCacheRead()
                        .setHashRequested(hash)
                        .setEvaluation(evaluation)
                );
            }
        }
    }
}
