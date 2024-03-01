package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.GameEvaluatorCacheRead;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationEval;

import java.util.Optional;

/**
 * @author Mauricio Coria
 */

public class TrapReadFromCache implements GameEvaluatorCacheRead, SearchByCycleListener {

    @Getter
    @Setter
    private SearchTracker searchTracker;

    @Getter
    @Setter
    private GameEvaluatorCacheRead gameEvaluatorCacheRead;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.searchTracker = context.getSearchTracker();
    }

    @Override
    public Integer readFromCache(long hash) {
        Integer evaluation = gameEvaluatorCacheRead.readFromCache(hash);
        if (evaluation != null) {
            trackReadFromCache(hash, evaluation);
        }
        return evaluation;
    }


    public void trackReadFromCache(long hash, Integer evaluation) {
        DebugNode currentNode = searchTracker.getCurrentNode();
        if (currentNode != null) {
            Optional<DebugOperationEval> previousReadOpt = currentNode.getEvalCacheReads().stream()
                    .filter(debugOperationEval -> debugOperationEval.getHashRequested() == hash)
                    .findFirst();

            if (previousReadOpt.isPresent()) {
                DebugOperationEval previousReadOpEval = previousReadOpt.get();
                if (previousReadOpEval.getEvaluation() != evaluation) {
                    throw new RuntimeException("Lectura repetida pero distinto valor retornado");
                }
            } else {
                currentNode.getEvalCacheReads().add(new DebugOperationEval()
                        .setHashRequested(hash)
                        .setEvaluation(evaluation)
                );
            }
        }
    }
}
