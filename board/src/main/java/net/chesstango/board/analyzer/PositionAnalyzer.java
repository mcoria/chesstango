package net.chesstango.board.analyzer;

import lombok.Setter;
import net.chesstango.board.GameListener;
import net.chesstango.board.GameStatus;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.generators.legal.LegalMoveGenerator;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.GameState;
import net.chesstango.board.position.GameStateReader;

import java.util.Iterator;

/**
 * PositionAnalyzer is responsible for analyzing the current state of a chess game,
 * updating the game state, and notifying listeners of moves being executed or undone.
 * <p>
 * This class uses various analyzers to determine the state of the game,
 * such as pinned pieces and king safety. It also supports rules like
 * threefold repetition and the fifty-move rule. It implements the GameListener
 * to update the GameState ofter a move execution.
 *
 * @author Mauricio Coria
 */
@Setter
public class PositionAnalyzer implements GameListener {
    private Analyzer pinnedAnalyzer;

    private Analyzer kingSafePositionsAnalyzer;

    private GameState gameState;

    private ChessPositionReader positionReader;

    private LegalMoveGenerator legalMoveGenerator;

    private boolean threefoldRepetitionRule;

    private boolean fiftyMovesRule;

    public void updateGameState() {
        AnalyzerResult analysis = analyze();

        MoveContainerReader<Move> legalMoves = legalMoveGenerator.getLegalMoves(analysis);

        int repetitionCounter = calculateRepetitionCounter();
        GameStatus gameStatus = calculateGameStatus(analysis, legalMoves, repetitionCounter);

        gameState.setStatus(gameStatus);
        gameState.setAnalyzerResult(analysis);
        gameState.setZobristHash(positionReader.getZobristHash());
        gameState.setPositionHash(positionReader.getAllPositions());
        gameState.setRepetitionCounter(repetitionCounter);
        gameState.setLegalMoves(legalMoves);
        gameState.setLegalMoves(gameStatus.isInProgress() ? legalMoves : new MoveContainer<>());
    }

    @Override
    public void notifyDoMove(Move move) {
        gameState.setSelectedMove(move);

        gameState.push();

        updateGameState();
    }

    @Override
    public void notifyUndoMove(Move move) {
        gameState.pop();

        gameState.setSelectedMove(null);
    }

    protected AnalyzerResult analyze() {

        AnalyzerResult result = new AnalyzerResult();

        pinnedAnalyzer.analyze(result);

        kingSafePositionsAnalyzer.analyze(result);

        return result;
    }

    private GameStatus calculateGameStatus(AnalyzerResult analysis, MoveContainerReader<Move> legalMoves, int repetitionCounter) {
        if (!legalMoves.isEmpty()) {
            if (fiftyMovesRule && positionReader.getHalfMoveClock() >= 100) {
                return GameStatus.DRAW_BY_FIFTY_RULE;
            } else if (threefoldRepetitionRule && positionReader.getHalfMoveClock() >= 8) {
                if (repetitionCounter > 2) {
                    return GameStatus.DRAW_BY_FOLD_REPETITION;
                }
            }
            return analysis.isKingInCheck() ? GameStatus.CHECK : GameStatus.NO_CHECK;
        }
        return analysis.isKingInCheck() ? GameStatus.MATE : GameStatus.STALEMATE;
    }

    private int calculateRepetitionCounter() {
        final long zobristHash = positionReader.getZobristHash();
        final long positionHash = positionReader.getAllPositions();

        int repetitionCounter = 1;
        int halfMoveClockCounter = positionReader.getHalfMoveClock();


        Iterator<GameStateReader> gameStateIterator = gameState.stateIterator();

        // Start iterating
        while (gameStateIterator.hasNext() && halfMoveClockCounter >= 0) {
            // Skip next state, opponent turn
            gameStateIterator.next();
            halfMoveClockCounter--;


            if (gameStateIterator.hasNext() && halfMoveClockCounter >= 0) {
                // Get next state, my turn
                GameStateReader currentState = gameStateIterator.next();
                halfMoveClockCounter--;

                if (currentState.getZobristHash() == zobristHash && currentState.getPositionHash() == positionHash) {
                    repetitionCounter = currentState.getRepetitionCounter() + 1;
                    break;
                }
            }
        }

        return repetitionCounter;
    }
}
