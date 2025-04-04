package net.chesstango.board.internal.position;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.position.GameState;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.GameStatus;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.representations.fen.FEN;

/**
 * Implementation of the GameState interface that manages the state of a chess game.
 * This class provides methods to get and set various game state attributes such as
 * game status, legal moves, selected move, analyzer result, and hash values.
 * It also supports pushing and popping game states to handle state changes.
 *
 * The GameStateData inner class holds the actual state data.
 *
 * @author Mauricio Coria
 */
public class GameStateImp implements GameState {

    private GameStateData currentGameState = new GameStateData();

    @Setter
    @Getter
    private FEN initialFEN;

    /**
     * Gets the current game status.
     *
     * @return the current game status
     */
    @Override
    public GameStatus getStatus() {
        return currentGameState.gameStatus;
    }

    /**
     * Sets the current game status.
     *
     * @param gameStatus the new game status
     */
    @Override
    public void setStatus(GameStatus gameStatus) {
        currentGameState.gameStatus = gameStatus;
    }

    /**
     * Gets the legal moves available in the current game state.
     *
     * @return the legal moves
     */
    @Override
    public MoveContainerReader<Move> getLegalMoves() {
        return currentGameState.legalMoves;
    }

    /**
     * Sets the legal moves available in the current game state.
     *
     * @param legalMoves the new legal moves
     */
    @Override
    public void setLegalMoves(MoveContainerReader<Move> legalMoves) {
        currentGameState.legalMoves = legalMoves;
    }

    /**
     * Gets the selected move in the current game state.
     *
     * @return the selected move
     */
    @Override
    public Move getSelectedMove() {
        return currentGameState.selectedMove;
    }

    /**
     * Sets the selected move in the current game state.
     *
     * @param selectedMove the new selected move
     */
    @Override
    public void setSelectedMove(Move selectedMove) {
        currentGameState.selectedMove = selectedMove;
    }

    /**
     * Gets the analyzer result for the current game state.
     *
     * @return the analyzer result
     */
    @Override
    public AnalyzerResult getAnalyzerResult() {
        return currentGameState.analyzerResult;
    }

    /**
     * Sets the analyzer result for the current game state.
     *
     * @param analyzerResult the new analyzer result
     */
    @Override
    public void setAnalyzerResult(AnalyzerResult analyzerResult) {
        currentGameState.analyzerResult = analyzerResult;
    }

    /**
     * Sets the Zobrist hash for the current game state.
     *
     * @param zobristHash the new Zobrist hash
     */
    @Override
    public void setZobristHash(long zobristHash) {
        currentGameState.zobristHash = zobristHash;
    }

    /**
     * Gets the Zobrist hash for the current game state.
     *
     * @return the Zobrist hash
     */
    @Override
    public long getZobristHash() {
        return currentGameState.zobristHash;
    }

    /**
     * Sets the position hash for the current game state.
     *
     * @param positionHash the new position hash
     */
    @Override
    public void setPositionHash(long positionHash) {
        currentGameState.positionHash = positionHash;
    }

    /**
     * Gets the position hash for the current game state.
     *
     * @return the position hash
     */
    @Override
    public long getPositionHash() {
        return currentGameState.positionHash;
    }

    /**
     * Gets the repetition counter for the current game state.
     *
     * @return the repetition counter
     */
    @Override
    public int getRepetitionCounter() {
        return currentGameState.repetitionCounter;
    }

    /**
     * Sets the repetition counter for the current game state.
     *
     * @param repetitionCounter the new repetition counter
     */
    @Override
    public void setRepetitionCounter(int repetitionCounter) {
        currentGameState.repetitionCounter = repetitionCounter;
    }

    /**
     * Gets the previous game state.
     *
     * @return the previous game state
     */
    @Override
    public GameStateReader getPreviousState() {
        return currentGameState.previousGameState;
    }

    /**
     * Pushes the current game state onto the stack and creates a new game state.
     */
    @Override
    public void push() {
        GameStateData previousGameState = currentGameState;
        currentGameState = new GameStateData();
        currentGameState.previousGameState = previousGameState;
    }

    /**
     * Pops the previous game state from the stack.
     */
    @Override
    public void pop() {
        currentGameState = currentGameState.previousGameState;
    }

    /**
     * Inner class that holds the actual game state data.
     */
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