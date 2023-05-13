package net.chesstango.search.smart;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements SearchMove {
    private final SearchSmart searchMove;

    public IterativeDeepening(SearchSmart searchMove) {
        this.searchMove = searchMove;
    }

    @Override
    public SearchMoveResult search(final Game game, final int depth) {
        List<SearchMoveResult> bestMovesByDepth = new ArrayList<>();

        int[] visitedNodesCounters = new int[30];
        int[] expectedNodesCounters = new int[30];
        int[] visitedNodesQuiescenceCounter = new int[30];
        Set<Move>[] distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> distinctMovesPerLevel[i] = new HashSet<>());
        Map<Long, SearchContext.TableEntry> maxMap = new HashMap<>();
        Map<Long, SearchContext.TableEntry> minMap = new HashMap<>();
        Map<Long, SearchContext.TableEntry> qMaxMap = new HashMap<>();
        Map<Long, SearchContext.TableEntry> qMinMap = new HashMap<>();


        try {
            for (int i = 1; i <= depth; i++) {

                SearchContext context = new SearchContext(game,
                        i,
                        visitedNodesCounters,
                        expectedNodesCounters,
                        visitedNodesQuiescenceCounter,
                        distinctMovesPerLevel,
                        maxMap,
                        minMap,
                        qMaxMap,
                        qMinMap);

                SearchMoveResult searchResult = searchMove.search(context);

                bestMovesByDepth.add(searchResult);

                if (GameEvaluator.WHITE_WON == searchResult.getEvaluation() || GameEvaluator.BLACK_WON == searchResult.getEvaluation()) {
                    break;
                }
            }
        } catch (StopSearchingException spe) {

        }

        SearchMoveResult lastSearch = bestMovesByDepth.get(bestMovesByDepth.size() - 1);


        return lastSearch;
    }


    @Override
    public void stopSearching() {
        searchMove.stopSearching();
    }

    @Override
    public void reset() {
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
