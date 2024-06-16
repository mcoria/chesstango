package net.chesstango.search.gamegraph;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;

/**
 * @author Mauricio Coria
 */
public class MockEvaluator implements Evaluator {

    @Getter
    private int nodesEvaluated = 0;

    private GameMock gameMock;

    @Override
    public int evaluate() {
        nodesEvaluated++;

        return gameMock.currentMockNode.evaluation;
    }

    @Override
    public void setGame(Game game) {
        this.gameMock = (GameMock) game;
    }

}
