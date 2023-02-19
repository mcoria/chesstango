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
 *
 * Almacena tanto el estado actual como estados anteriores.
 */
public class GameState {

    private final Deque<GameStateData> stackGameStates = new ArrayDeque<GameStateData>();
    private GameStateData currentGameState = new GameStateData();

    public GameStatus getStatus() {
        return currentGameState.gameStatus;
    }

    public void setStatus(GameStatus gameStatus) {
        currentGameState.gameStatus = gameStatus;
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
        gameStateData.gameStatus = currentGameState.gameStatus;
        gameStateData.legalMoves = currentGameState.legalMoves;
        gameStateData.fenWithoutClocks = currentGameState.fenWithoutClocks;

        stackGameStates.push(gameStateData);

        currentGameState.selectedMove = null;
        currentGameState.analyzerResult = null;
        currentGameState.gameStatus = null;
        currentGameState.legalMoves = null;
        currentGameState.fenWithoutClocks = null;
    }

    public void pop() {
        GameStateData lastState = stackGameStates.pop();

        currentGameState = lastState;
    }

    public void accept(GameStateVisitor gameStateVisitor) {
        gameStateVisitor.visit(this);

        Iterator<GameStateData> iterator = stackGameStates.descendingIterator();

        while(iterator.hasNext()){
            GameStateData gameStateDate = iterator.next();

            gameStateVisitor.visit(gameStateDate);
        }

        gameStateVisitor.visit(currentGameState);
    }

    public static class GameStateData {
        public AnalyzerResult analyzerResult;
        public MoveContainerReader legalMoves;
        public Move selectedMove;
        public GameStatus gameStatus;
        public String fenWithoutClocks;
    }
}