package net.chesstango.search.gamegraph;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;

public class GameMockEvaluator implements GameEvaluator {

    @Override
    public int evaluate(Game game) {
        GameMock gameMove = (GameMock)game;

        return gameMove.currentMockNode.evaluation;
    }
}
