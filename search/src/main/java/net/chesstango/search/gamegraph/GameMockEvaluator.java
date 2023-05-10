package net.chesstango.search.gamegraph;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 */
public class GameMockEvaluator implements GameEvaluator {

    private int nodesEvaluated = 0;

    @Override
    public int evaluate(Game game) {
        nodesEvaluated++;

        GameMock gameMove = (GameMock) game;

        return gameMove.currentMockNode.evaluation;
    }

    public int getNodesEvaluated() {
        return nodesEvaluated;
    }
}
