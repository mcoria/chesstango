/**
 *
 */
package net.chesstango.board;

import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class GameState {

    public enum Status {
        NO_CHECK(true),
        CHECK(true),
        MATE(false),
        DRAW(false),
        DRAW_BY_FIFTY_RULE(false),
        DRAW_BY_FOLD_REPETITION(false);

        private final boolean inProgress;

        Status(boolean inProgress) {
            this.inProgress = inProgress;
        }

        public boolean isInProgress() {
            return inProgress;
        }

        public boolean isFinalStatus(){
            return !isInProgress();
        }
    }

    public static class GameStateData {
        public AnalyzerResult analyzerResult;
        public MoveContainerReader legalMoves;
        public Move selectedMove;
        public Status status;
        public String fenWithoutClocks;
    }

    private final Deque<GameStateData> stackGameStates = new ArrayDeque<GameStateData>();
    private GameStateData currentGameState = new GameStateData();

    public Iterator<GameStateData> iterateGameStates() {
        return stackGameStates.descendingIterator();
    }

    public Status getStatus() {
        return currentGameState.status;
    }

    public void setStatus(Status status) {
        currentGameState.status = status;
    }


    public MoveContainerReader getLegalMoves() {
        return currentGameState.legalMoves;
    }

    public void setLegalMoves(MoveContainerReader legalMoves) {
        currentGameState.legalMoves = legalMoves;
    }

    public Move getSelectedMove() {
        return currentGameState.selectedMove;
    }

    public void setSelectedMove(Move selectedMove) {
        currentGameState.selectedMove = selectedMove;
    }


    public AnalyzerResult getAnalyzerResult() {
        return currentGameState.analyzerResult;
    }

    public void setAnalyzerResult(AnalyzerResult analyzerResult) {
        currentGameState.analyzerResult = analyzerResult;
    }

    public void setFenWithoutClocks(String fenWithoutClocks) {
        currentGameState.fenWithoutClocks = fenWithoutClocks;
    }

    public String getFenWithoutClocks() {
        return currentGameState.fenWithoutClocks;
    }

    public void push() {
        GameStateData gameStateData = new GameStateData();
        gameStateData.selectedMove = currentGameState.selectedMove;
        gameStateData.analyzerResult = currentGameState.analyzerResult;
        gameStateData.status = currentGameState.status;
        gameStateData.legalMoves = currentGameState.legalMoves;
        gameStateData.fenWithoutClocks = currentGameState.fenWithoutClocks;

        stackGameStates.push(gameStateData);

        currentGameState.selectedMove = null;
        currentGameState.analyzerResult = null;
        currentGameState.status = null;
        currentGameState.legalMoves = null;
        currentGameState.fenWithoutClocks = null;
    }

    public void pop() {
        GameStateData lastState = stackGameStates.pop();

        currentGameState = lastState;
    }

}