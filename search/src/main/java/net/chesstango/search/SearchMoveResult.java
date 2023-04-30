package net.chesstango.search;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.SANEncoder;
import net.chesstango.search.smart.SearchContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class SearchMoveResult {
    private final int depth;
    private final int evaluation;
    private final Move bestMove;
    private final Move ponderMove;
    private int evaluationCollisions;
    private int[] visitedNodesCounters;
    private int[] visitedNodesQuiescenceCounter;
    private int[] expectedNodesCounters;
    private int[] evaluatedNodes;
    private Set<Move>[] distinctMovesPerLevel;
    private List<Move> bestMoveOptions;
    private List<String> principalVariation;

    public SearchMoveResult(int depth, int evaluation, Move bestMove, Move ponderMove) {
        this.depth = depth;
        this.evaluation = evaluation;
        this.bestMove = bestMove;
        this.ponderMove = ponderMove;
    }

    public int getDepth() {
        return depth;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public int getEvaluationCollisions() {
        return evaluationCollisions;
    }

    public SearchMoveResult setEvaluationCollisions(int evaluationCollisions) {
        this.evaluationCollisions = evaluationCollisions;
        return this;
    }

    public Move getBestMove() {
        return bestMove;
    }

    public Move getPonderMove() {
        return ponderMove;
    }

    public int[] getVisitedNodesCounters() {
        return visitedNodesCounters;
    }

    public SearchMoveResult setVisitedNodesCounters(int[] visitedNodesCounters) {
        this.visitedNodesCounters = visitedNodesCounters;
        return this;
    }

    public Set<Move>[] getDistinctMovesPerLevel() {
        return distinctMovesPerLevel;
    }

    public SearchMoveResult setDistinctMovesPerLevel(Set<Move>[] distinctMovesPerLevel) {
        this.distinctMovesPerLevel = distinctMovesPerLevel;
        return this;
    }

    public int[] getExpectedNodesCounters() {
        return expectedNodesCounters;
    }

    public SearchMoveResult setExpectedNodesCounters(int[] expectedNodesCounters) {
        this.expectedNodesCounters = expectedNodesCounters;
        return this;
    }

    public List<Move> getBestMoveOptions() {
        return bestMoveOptions;
    }

    public SearchMoveResult setBestMoveOptions(List<Move> bestMoveOptions) {
        this.bestMoveOptions = bestMoveOptions;
        return this;
    }

    public int[] getEvaluatedNodes() {
        return evaluatedNodes;
    }

    public SearchMoveResult setEvaluatedNodes(int[] evaluatedNodes) {
        this.evaluatedNodes = evaluatedNodes;
        return this;
    }

    public int[] getVisitedNodesQuiescenceCounter() {
        return visitedNodesQuiescenceCounter;
    }

    public SearchMoveResult setVisitedNodesQuiescenceCounter(int[] visitedNodesQuiescenceCounter) {
        this.visitedNodesQuiescenceCounter = visitedNodesQuiescenceCounter;
        return this;
    }

    public List<String> getPrincipalVariation() {
        return principalVariation;
    }

    public void calculatePrincipalVariation(Game game, int depth,
                                            Map<Long, SearchContext.TableEntry> maxMap,
                                            Map<Long, SearchContext.TableEntry> minMap,
                                            Map<Long, Long> qMaxMap,
                                            Map<Long, Long> qMinMap) {

        List<String> principalVariation = new ArrayList<>();

        Move move = bestMove;

        SANEncoder sanEncoder = new SANEncoder();
        do {

            principalVariation.add(sanEncoder.encode(move, game.getPossibleMoves()));

            game.executeMove(move);

            move =   principalVariation.size() < depth ? readMoveFromTT(game, maxMap, minMap): readMoveFromQTT(game, qMaxMap, qMinMap);

        } while (move != null);

        final int pvMoveCounter = principalVariation.size();

        for (int i = 0; i < pvMoveCounter; i++) {
            game.undoMove();
        }

        this.principalVariation = principalVariation;
    }

    private Move readMoveFromTT(Game game, Map<Long, SearchContext.TableEntry> maxMap, Map<Long, SearchContext.TableEntry> minMap) {
        Move result = null;

        long hash = game.getChessPosition().getPositionHash();

        SearchContext.TableEntry entry = null;

        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            entry = maxMap.get(hash);
        } else {
            entry = minMap.get(hash);
        }

        if (entry != null) {
            short bestMoveEncoded = (short) (entry.bestMoveAndValue >> 32);
            for (Move posibleMove : game.getPossibleMoves()) {
                if (posibleMove.binaryEncoding() == bestMoveEncoded) {
                    result = posibleMove;
                    break;
                }
            }
            if (result == null) {
                throw new RuntimeException("BestMove not found");
            }
        }

        return result;
    }

    private Move readMoveFromQTT(Game game, Map<Long, Long> qMaxMap, Map<Long, Long> qMinMap) {
        Move result = null;

        long hash = game.getChessPosition().getPositionHash();

        Long bestMoveAndValue = null;

        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            bestMoveAndValue = qMaxMap.get(hash);
        } else {
            bestMoveAndValue = qMinMap.get(hash);
        }

        if (bestMoveAndValue != null) {
            short bestMoveEncoded = (short) (bestMoveAndValue >> 32);
            if(bestMoveEncoded != 0) {
                for (Move posibleMove : game.getPossibleMoves()) {
                    if (posibleMove.binaryEncoding() == bestMoveEncoded) {
                        result = posibleMove;
                        break;
                    }
                }
                if (result == null) {
                    throw new RuntimeException("BestMove not found");
                }
            }
        }

        return result;
    }
}
