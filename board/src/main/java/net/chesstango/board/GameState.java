package net.chesstango.board;

import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;

/**
 * @author Mauricio Coria
 * <p>
 * Almacena tanto el estado actual como estados anteriores.
 */
public class GameState implements GameStateReader, GameStateWriter {

    private GameStateData currentGameState = new GameStateData();
    private String initialFEN;

    @Override
    public GameStatus getStatus() {
        return currentGameState.gameStatus;
    }

    @Override
    public void setStatus(GameStatus gameStatus) {
        currentGameState.gameStatus = gameStatus;
    }

    @Override
    public MoveContainerReader getLegalMoves() {
        return currentGameState.legalMoves;
    }

    @Override
    public void setLegalMoves(MoveContainerReader legalMoves) {
        currentGameState.legalMoves = legalMoves;
    }

    @Override
    public Move getSelectedMove() {
        return currentGameState.selectedMove;
    }

    @Override
    public void setSelectedMove(Move selectedMove) {
        currentGameState.selectedMove = selectedMove;
    }

    @Override
    public AnalyzerResult getAnalyzerResult() {
        return currentGameState.analyzerResult;
    }

    @Override
    public void setAnalyzerResult(AnalyzerResult analyzerResult) {
        currentGameState.analyzerResult = analyzerResult;
    }

    @Override
    public void setZobristHash(long zobristHash) {
        currentGameState.zobristHash = zobristHash;
    }

    @Override
    public long getZobristHash() {
        return currentGameState.zobristHash;
    }

    @Override
    public void setPositionHash(long positionHash) {
        currentGameState.positionHash = positionHash;
    }

    @Override
    public long getPositionHash() {
        return currentGameState.positionHash;
    }

    @Override
    public GameStateReader getPreviousState() {
        return currentGameState.previousGameState;
    }

    public void setInitialFEN(String initialFEN) {
        this.initialFEN = initialFEN;
    }

    public String getInitialFen() {
        return initialFEN;
    }

    public void push() {
        GameStateData previousGameState = currentGameState;
        currentGameState = new GameStateData();
        currentGameState.previousGameState = previousGameState;
    }

    public void pop() {
        currentGameState = currentGameState.previousGameState;
    }

    private static class GameStateData implements GameStateReader {
        protected AnalyzerResult analyzerResult;
        protected MoveContainerReader legalMoves;
        protected Move selectedMove;
        protected GameStatus gameStatus;
        protected long zobristHash;
        protected long positionHash;
        protected GameStateData previousGameState = null;

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
        public long getZobristHash() {
            return zobristHash;
        }

        @Override
        public long getPositionHash() {
            return positionHash;
        }

        @Override
        public GameStateData getPreviousState() {
            return previousGameState;
        }
    }
}