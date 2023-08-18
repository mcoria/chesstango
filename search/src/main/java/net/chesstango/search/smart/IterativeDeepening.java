package net.chesstango.search.smart;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchInfo;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements SearchMove {
    private final SearchSmart searchSmart;
    private Consumer<SearchInfo> searchStatusListener;

    public IterativeDeepening(SearchSmart searchSmartAlgorithm) {
        this.searchSmart = searchSmartAlgorithm;
    }

    @Override
    public SearchMoveResult search(final Game game, final int depth) {
        List<SearchMoveResult> bestMovesByDepth = new ArrayList<>();

        searchSmart.beforeSearch(game, depth);

        try {

            for (int currentSearchDepth = 1; currentSearchDepth <= depth; currentSearchDepth++) {

                SearchContext context = new SearchContext(currentSearchDepth);

                searchSmart.beforeSearchByDepth(context);

                SearchMoveResult searchResult = searchSmart.search(context);

                searchSmart.afterSearchByDepth(searchResult);

                bestMovesByDepth.add(searchResult);

                if (searchStatusListener != null) {
                    SearchInfo searchInfo = new SearchInfo(currentSearchDepth, currentSearchDepth, searchResult.getPrincipalVariation());
                    searchStatusListener.accept(searchInfo);
                }

                if (GameEvaluator.WHITE_WON == searchResult.getEvaluation() || GameEvaluator.BLACK_WON == searchResult.getEvaluation()) {
                    break;
                }
            }

        } catch (StopSearchingException spe) {

            searchSmart.afterSearchByDepth(null);

            SearchMoveResult bestMove = bestMovesByDepth.get(bestMovesByDepth.size() - 1);  // Aca deberiamos buscar en TT el mejor

            spe.setSearchMoveResult(bestMove);

            searchSmart.afterSearch(bestMove);

            throw spe;
        }

        SearchMoveResult bestMove = bestMovesByDepth.get(bestMovesByDepth.size() - 1);

        searchSmart.afterSearch(bestMove);

        return bestMove;
    }


    @Override
    public void stopSearching() {
        this.searchSmart.stopSearching();
    }

    @Override
    public void reset() {
        this.searchSmart.reset();
    }

    public void setSearchStatusListener(Consumer<SearchInfo> searchStatusListener) {
        this.searchStatusListener = searchStatusListener;
    }

    /**
     * En caso que la busqueda devuelva mas de una opcion para una profundidad dada seleccionamos la opcion de
     * mayor ocurrencia considerando las busquedas de profundidad menor.
     *
     * @param currentTurn
     * @param bestMovesByDepth
     * @return
     */
    protected Move selectBestMove(Color currentTurn, List<SearchMoveResult> bestMovesByDepth) {
        List<Move> lastSearchMoveOptions = bestMovesByDepth.get(bestMovesByDepth.size() - 1).getBestMoveOptions();

        int maxDepth = bestMovesByDepth.size();

        Map<Move, Integer> moveByFrequency = new HashMap<>();
        lastSearchMoveOptions.stream().forEach(move -> moveByFrequency.put(move, maxDepth));
        for (int i = 0; i < bestMovesByDepth.size(); i++) {
            final Integer currentDepth = i + 1; // Depth provides extra points
            List<Move> byDepthOptions = bestMovesByDepth.get(i).getBestMoveOptions();
            byDepthOptions.stream().filter(lastSearchMoveOptions::contains).forEach(move -> moveByFrequency.compute(move, (k, v) -> v + currentDepth));
        }

        int maxFrequency = moveByFrequency.values().stream().mapToInt(Integer::intValue).max().getAsInt();

        List<Move> filteredOptions = moveByFrequency.entrySet().stream().filter(entry -> entry.getValue().equals(maxFrequency)).map(Map.Entry::getKey).collect(Collectors.toList());

        return MoveSelector.selectMove(currentTurn, filteredOptions);
    }

}
