package net.chesstango.search.smart;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements SearchMove {
    private final SearchSmart searchSmart;
    private SearchStatusListener searchStatusListener;
    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;

    private volatile boolean keepProcessing;

    public IterativeDeepening(SearchSmart searchSmartAlgorithm) {
        this.searchSmart = searchSmartAlgorithm;
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
    }

    @Override
    public SearchMoveResult search(final Game game, final int depth) {
        this.keepProcessing = true;
        List<SearchMoveResult> bestMovesByDepth = new ArrayList<>();

        int[] visitedNodesCounters = new int[30];
        int[] expectedNodesCounters = new int[30];
        int[] visitedNodesQuiescenceCounter = new int[30];
        Set<Move>[] distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> distinctMovesPerLevel[i] = new HashSet<>());

        try {
            for (int currentSearchDepth = 1; currentSearchDepth <= depth && keepProcessing; currentSearchDepth++) {

                SearchContext context = new SearchContext(game,
                        currentSearchDepth,
                        visitedNodesCounters,
                        expectedNodesCounters,
                        visitedNodesQuiescenceCounter,
                        distinctMovesPerLevel,
                        maxMap,
                        minMap);

                SearchMoveResult searchResult = searchSmart.search(context);

                bestMovesByDepth.add(searchResult);

                if(searchStatusListener != null){
                    searchStatusListener.info(currentSearchDepth, currentSearchDepth, searchResult.getPrincipalVariation());
                }

                if (GameEvaluator.WHITE_WON == searchResult.getEvaluation() || GameEvaluator.BLACK_WON == searchResult.getEvaluation()) {
                    break;
                }
            }

            SearchMoveResult bestMove = bestMovesByDepth.get(bestMovesByDepth.size() - 1);

            return bestMove;

        } catch (StopSearchingException spe) {
            SearchMoveResult bestMove = bestMovesByDepth.get(bestMovesByDepth.size() - 1);  // Aca deberiamos buscar en TT el mejor

            spe.setSearchMoveResult(bestMove);

            throw spe;

        } catch (CycleException ce) {
            SearchMoveResult bestMove = bestMovesByDepth.get(bestMovesByDepth.size() - 1);

           return bestMove;
        }
    }


    @Override
    public void stopSearching() {
        this.keepProcessing = false;
        this.searchSmart.stopSearching();
    }

    @Override
    public void reset() {
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
    }

    public void setSearchStatusListener(SearchStatusListener searchStatusListener) {
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
