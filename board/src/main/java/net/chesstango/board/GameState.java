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
    private GameStateData previosGameState = null;
    private String initialFEN;

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
        stackGameStates.push(currentGameState);

        previosGameState = currentGameState;
        currentGameState = new GameStateData();
    }

    public void pop() {
        GameStateData lastState = stackGameStates.pop();

        currentGameState = lastState;

        previosGameState = stackGameStates.size() > 0 ? stackGameStates.getLast() : null;
    }

    public void accept(GameVisitor gameVisitor) {
        gameVisitor.visit(this);

        Iterator<GameStateData> iterator = stackGameStates.descendingIterator();

        while(iterator.hasNext()){
            GameStateData gameStateDate = iterator.next();

            gameVisitor.visit(gameStateDate);
        }

        gameVisitor.visit(currentGameState);
    }


    public void setInitialFEN(String initialFEN) {
        this.initialFEN = initialFEN;
    }

    public String getInitialFen() {
        return initialFEN;
    }

    public GameStateData getPreviosGameState() {
        return previosGameState;
    }

    public static class GameStateData {
        public AnalyzerResult analyzerResult;
        public MoveContainerReader legalMoves;
        public Move selectedMove;
        public GameStatus gameStatus;
        public String fenWithoutClocks;
    }
}