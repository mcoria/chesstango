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
public class GameState implements GameStateReader {

    private final Deque<GameStateData> stackGameStates = new ArrayDeque<GameStateData>();
    private GameStateData currentGameState = new GameStateData();
    private String initialFEN;

    @Override
    public GameStatus getStatus() {
        return currentGameState.gameStatus;
    }

    public void setStatus(GameStatus gameStatus) {
        currentGameState.gameStatus = gameStatus;
    }

    @Override
    public MoveContainerReader getLegalMoves() {
        return currentGameState.legalMoves;
    }

    public void setLegalMoves(MoveContainerReader legalMoves) {
        currentGameState.legalMoves = legalMoves;
    }

    @Override
    public Move getSelectedMove() {
        return currentGameState.selectedMove;
    }

    public void setSelectedMove(Move selectedMove) {
        currentGameState.selectedMove = selectedMove;
    }

    @Override
    public AnalyzerResult getAnalyzerResult() {
        return currentGameState.analyzerResult;
    }

    public void setAnalyzerResult(AnalyzerResult analyzerResult) {
        currentGameState.analyzerResult = analyzerResult;
    }

    public void setFenWithoutClocks(String fenWithoutClocks) {
        currentGameState.fenWithoutClocks = fenWithoutClocks;
    }

    @Override
    public String getFenWithoutClocks() {
        return currentGameState.fenWithoutClocks;
    }

    @Override
    public GameStateReader getPreviosState() {
        return currentGameState.previosGameState;
    }

    public void setInitialFEN(String initialFEN) {
        this.initialFEN = initialFEN;
    }

    public String getInitialFen() {
        return initialFEN;
    }

    public void push() {
        stackGameStates.push(currentGameState);

        GameStateData previosGameState = currentGameState;
        currentGameState = new GameStateData();
        currentGameState.previosGameState = previosGameState;
    }

    public void pop() {
        GameStateData lastState = stackGameStates.pop();

        currentGameState = lastState;
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

    private static class GameStateData implements GameStateReader {
        protected AnalyzerResult analyzerResult;
        protected MoveContainerReader legalMoves;
        protected Move selectedMove;
        protected GameStatus gameStatus;
        protected String fenWithoutClocks;
        protected GameStateData previosGameState = null;

        @Override
        public GameStatus getStatus() {
            return gameStatus;
        }

        @Override
        public MoveContainerReader getLegalMoves() {
            return legalMoves;
        }

        @Override
        public Move getSelectedMove() {
            return selectedMove;
        }

        @Override
        public AnalyzerResult getAnalyzerResult() {
            return analyzerResult;
        }

        @Override
        public String getFenWithoutClocks() {
            return fenWithoutClocks;
        }

        @Override
        public GameStateData getPreviosState() {
            return previosGameState;
        }
    }
}