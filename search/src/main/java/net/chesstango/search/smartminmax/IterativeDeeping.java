package net.chesstango.search.smartminmax;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class IterativeDeeping implements SearchMove {
    protected boolean keepProcessing;

    private final SearchMove imp;

    private final List<SearchMoveResult> bestMovesByDepth;

    public IterativeDeeping() {
        this(new MinMaxPruning());
    }

    public IterativeDeeping(SearchMove minMax) {
        this.imp = minMax;
        this.bestMovesByDepth = new ArrayList<>();
    }

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, int depth) {
        keepProcessing = true;
        bestMovesByDepth.clear();
        for (int i = 1; i <= depth; i++) {

            SearchMoveResult searchResult = imp.searchBestMove(game, i);

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
                break;
            }
        }
        return bestMovesByDepth.get(bestMovesByDepth.size() - 1);
    }

    @Override
    public void stopSearching() {
        keepProcessing = false;
        imp.stopSearching();
    }

    @Override
    public void setGameEvaluator(GameEvaluator evaluator) {
        imp.setGameEvaluator(evaluator);
    }


}
