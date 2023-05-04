package net.chesstango.search;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.SANEncoder;

import java.util.*;

import static net.chesstango.search.smart.SearchContext.TableEntry;

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
    private long evaluatedGamesCounter;
    private List<MoveEvaluation> moveEvaluationList;


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

    public long getEvaluatedGamesCounter() {
        return evaluatedGamesCounter;
    }

    public SearchMoveResult setEvaluatedGamesCounter(long evaluatedGamesCounter) {
        this.evaluatedGamesCounter = evaluatedGamesCounter;
        return this;
    }

    public List<String> getPrincipalVariation() {
        return principalVariation;
    }

    public Collection<MoveEvaluation> getMoveEvaluationList() {
        return moveEvaluationList;
    }

    public SearchMoveResult calculatePrincipalVariation(Game game, int depth,
                                                        Map<Long, TableEntry> maxMap,
                                                        Map<Long, TableEntry> minMap,
                                                        Map<Long, TableEntry> qMaxMap,
                                                        Map<Long, TableEntry> qMinMap) {

        List<String> principalVariation = new ArrayList<>();

        Move move = bestMove;

        SANEncoder sanEncoder = new SANEncoder();
        do {

            principalVariation.add(sanEncoder.encode(move, game.getPossibleMoves()));

            game.executeMove(move);

            move = principalVariation.size() < depth ? readMoveFromTT(game, maxMap, minMap) : readMoveFromQTT(game, qMaxMap, qMinMap);

        } while (move != null);

        final int pvMoveCounter = principalVariation.size();

        for (int i = 0; i < pvMoveCounter; i++) {
            game.undoMove();
        }

        this.principalVariation = principalVariation;

        return this;
    }

    private Move readMoveFromTT(Game game, Map<Long, TableEntry> maxMap, Map<Long, TableEntry> minMap) {
        Move result = null;

        long hash = game.getChessPosition().getPositionHash();

        TableEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.get(hash) : minMap.get(hash);


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

    private Move readMoveFromQTT(Game game, Map<Long, TableEntry> qMaxMap, Map<Long, TableEntry> qMinMap) {
        Move result = null;

        long hash = game.getChessPosition().getPositionHash();

        TableEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? qMaxMap.get(hash) : qMinMap.get(hash);

        if (entry != null) {
            short bestMoveEncoded = (short) (entry.bestMoveAndValue >> 32);
            if (bestMoveEncoded != 0) {
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

    public SearchMoveResult storeMoveEvaluations(Game game, Map<Long, TableEntry> maxMap, Map<Long, TableEntry> minMap) {
        List<MoveEvaluation> moveEvaluationList = new ArrayList<>();

        boolean bestMovePresent = false;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            long hash = game.getChessPosition().getPositionHash();

            TableEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.get(hash) : minMap.get(hash);

            if (entry != null) {
                MoveEvaluation moveEvaluation = new MoveEvaluation();
                moveEvaluation.move = move;
                moveEvaluation.evaluation = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & entry.bestMoveAndValue);
                moveEvaluationList.add(moveEvaluation);
            }

            if(move.equals(bestMove)){
                bestMovePresent = true;
            }

            game.undoMove();
        }

        if(!bestMovePresent){
            throw new RuntimeException("Best move is not present in game");
        }

        if (moveEvaluationList.isEmpty()) {
            MoveEvaluation moveEvaluation = new MoveEvaluation();
            moveEvaluation.move = this.bestMove;
            moveEvaluation.evaluation = this.evaluation;
        } else {
            OptionalInt bestEvaluation = null;
            if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
                bestEvaluation = moveEvaluationList.stream().mapToInt(me -> me.evaluation).max();
            } else {
                bestEvaluation = moveEvaluationList.stream().mapToInt(me -> me.evaluation).min();
            }
            if ( !bestEvaluation.isPresent() || bestEvaluation.getAsInt() != evaluation) {
                throw new RuntimeException("El mejor valor no coincide");
            }
        }

        this.moveEvaluationList = moveEvaluationList;

        return this;
    }

    public static class MoveEvaluation implements Comparable<MoveEvaluation> {
        public Move move;
        public int evaluation;

        @Override
        public int compareTo(MoveEvaluation other) {
            return evaluation - other.evaluation;
        }
    }

}
