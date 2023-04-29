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
    private final AbstractSmart searchMove;
    private volatile boolean keepProcessing;

    public IterativeDeepening(AbstractSmart searchMove) {
        this.searchMove = searchMove;
    }

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, int depth) {
        keepProcessing = true;
        Color currentTurn = game.getChessPosition().getCurrentTurn();
        List<SearchMoveResult> bestMovesByDepth = new ArrayList<>();

        int[] visitedNodesCounters = new int[30];
        int[] expectedNodesCounters = new int[30];
        int[] visitedNodesQuiescenceCounter = new int[30];
        Set<Move>[] distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> distinctMovesPerLevel[i] = new HashSet<>() );
        Map<Long, SearchContext.TableEntry> maxMap = new HashMap<>();
        Map<Long, SearchContext.TableEntry> minMap = new HashMap<>();

        for (int i = 1; i <= depth; i++) {
            SearchContext context = new SearchContext(i, visitedNodesCounters, expectedNodesCounters, visitedNodesQuiescenceCounter, distinctMovesPerLevel, maxMap, minMap);

            SearchMoveResult searchResult = searchMove.searchBestMove(game, context);

            if (keepProcessing) {
                bestMovesByDepth.add(searchResult);
                if (GameEvaluator.WHITE_WON == searchResult.getEvaluation() || GameEvaluator.BLACK_WON == searchResult.getEvaluation()) {
                    break;
                }
            } else {
                /*
                SearchMoveResult lastBestMove = bestMovesByDepth.get(bestMovesByDepth.size() - 1);
                if (searchResult.getEvaluation() >= lastBestMove.getEvaluation()) {
                    bestMovesByDepth.add(lastBestMove);
                }*/
                throw new RuntimeException("Unimplemented logic");
            }
        }

        SearchMoveResult lastSearch = bestMovesByDepth.get(bestMovesByDepth.size() - 1);

        Move bestMove = lastSearch.getBestMove();

        return new SearchMoveResult(depth, lastSearch.getEvaluation(), bestMove, null)
                .setVisitedNodesCounters(visitedNodesCounters)
                .setDistinctMovesPerLevel(distinctMovesPerLevel)
                .setExpectedNodesCounters(expectedNodesCounters)
                .setEvaluationCollisions(lastSearch.getEvaluationCollisions())
                .setBestMoveOptions(lastSearch.getBestMoveOptions());
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
            byDepthOptions.stream().filter( lastSearchMoveOptions::contains ).forEach( move -> moveByFrequency.compute(move, (k, v) -> v + currentDepth ));
        }

        int maxFrequency = moveByFrequency.values().stream().mapToInt(Integer::intValue).max().getAsInt();
        
        List<Move> filteredOptions = moveByFrequency.entrySet().stream().filter(entry -> entry.getValue().equals(maxFrequency)).map(Map.Entry::getKey).collect(Collectors.toList());

        return MoveSelector.selectMove(currentTurn, filteredOptions);
    }

    @Override
    public void stopSearching() {
        keepProcessing = false;
        searchMove.stopSearching();
    }

}
