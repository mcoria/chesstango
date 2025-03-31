package net.chesstango.board.position.imp;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.position.GameState;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.GameStatus;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.representations.fen.FEN;

/**
 * @author Mauricio Coria
 * <p>
 * Almacena tanto el estado actual como estados anteriores.
 */
public class GameStateImp implements GameState {

    private GameStateData currentGameState = new GameStateData();

    @Setter
    @Getter
    private FEN initialFEN;

    @Override
    public GameStatus getStatus() {
        return currentGameState.gameStatus;
    }

    @Override
    public void setStatus(GameStatus gameStatus) {
        currentGameState.gameStatus = gameStatus;
    }

    @Override
    public MoveContainerReader<Move> getLegalMoves() {
        return currentGameState.legalMoves;
    }

    @Override
    public void setLegalMoves(MoveContainerReader<Move> legalMoves) {
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
    public int getRepetitionCounter() {
        return currentGameState.repetitionCounter;
    }

    @Override
    public void setRepetitionCounter(int repetitionCounter) {
        currentGameState.repetitionCounter = repetitionCounter;
    }

    @Override
    public GameStateReader getPreviousState() {
        return currentGameState.previousGameState;
    }

    @Override
    public void push() {
        GameStateData previousGameState = currentGameState;
        currentGameState = new GameStateData();
        currentGameState.previousGameState = previousGameState;
    }

    @Override
    public void pop() {
        currentGameState = currentGameState.previousGameState;
    }

    private static class GameStateData implements GameStateReader {
        protected AnalyzerResult analyzerResult;
        protected MoveContainerReader<Move> legalMoves;
        protected Move selectedMove;
        protected GameStatus gameStatus;
        protected long zobristHash;
        protected long positionHash;
        protected int repetitionCounter;
        protected GameStateData previousGameState = null;

        @Override
        public GameStatus getStatus() {
            return gameStatus;
        }

        @Override
        public MoveContainerReader<Move> getLegalMoves() {
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
        public int getRepetitionCounter() {
            return repetitionCounter;
        }

        @Override
        public GameStateData getPreviousState() {
            return previousGameState;
        }
    }
}