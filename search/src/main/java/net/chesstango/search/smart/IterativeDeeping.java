package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class IterativeDeeping implements SearchMove {
    private final AbstractSmart searchMove;
    private boolean keepProcessing;

    public IterativeDeeping(AbstractSmart searchMove) {
        this.searchMove = searchMove;
    }

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, int depth) {
        keepProcessing = true;
        List<SearchMoveResult> bestMovesByDepth = new ArrayList<>();

        int[] visitedNodesCounters = new int[30];
        int[] expectedNodesCounters = new int[30];
        List<Set<Move>> distinctMovesPerLevel = new ArrayList<>(visitedNodesCounters.length);
        for (int i = 0; i < 30; i++) {
            distinctMovesPerLevel.add(new HashSet<>());
        }

        for (int i = 1; i <= depth; i++) {
            SearchContext context = new SearchContext(i)
                    .setVisitedNodesCounters(visitedNodesCounters)
                    .setDistinctMovesPerLevel(distinctMovesPerLevel)
                    .setExpectedNodesCounters(expectedNodesCounters);

            SearchMoveResult searchResult = searchMove.searchBestMove(game, context);

            if (keepProcessing) {
                bestMovesByDepth.add(searchResult);
                if (GameEvaluator.WHITE_WON == searchResult.getEvaluation() || GameEvaluator.BLACK_WON == searchResult.getEvaluation()) {
                    break;
                }
            } else {
                SearchMoveResult lastBestMove = bestMovesByDepth.get(bestMovesByDepth.size() - 1);
                if (searchResult.getEvaluation() >= lastBestMove.getEvaluation()) {
                    bestMovesByDepth.add(lastBestMove);
                }
                throw new RuntimeException("Unimplemented logic");
            }
        }

        SearchMoveResult lastSearch = bestMovesByDepth.get(bestMovesByDepth.size() - 1);

        return new SearchMoveResult(depth, lastSearch.getEvaluation(), lastSearch.getBestMove(), null)
                .setVisitedNodesCounters(visitedNodesCounters)
                .setDistinctMovesPerLevel(distinctMovesPerLevel)
                .setExpectedNodesCounters(expectedNodesCounters)
                .setEvaluationCollisions(lastSearch.getEvaluationCollisions());
    }

    @Override
    public void stopSearching() {
        keepProcessing = false;
        searchMove.stopSearching();
    }

}
