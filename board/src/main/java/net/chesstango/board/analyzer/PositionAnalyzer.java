package net.chesstango.board.analyzer;

import lombok.Setter;
import net.chesstango.board.GameListener;
import net.chesstango.board.Status;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.generators.legal.LegalMoveGenerator;
import net.chesstango.board.position.*;

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

    private GameHistoryReader careTaker;

    private GameState gameState;

    private PositionReader positionReader;

    private LegalMoveGenerator legalMoveGenerator;

    private boolean threefoldRepetitionRule;

    private boolean fiftyMovesRule;

    public void updateGameState() {
        AnalyzerResult analyzerResult = analyze();

        MoveContainerReader<Move> legalMoves = legalMoveGenerator.getLegalMoves(analyzerResult);

        int repetitionCounter = calculateRepetitionCounter();
        Status status = calculateGameStatus(analyzerResult, legalMoves, repetitionCounter);

        gameState.setStatus(status);
        gameState.setAnalyzerResult(analyzerResult);
        gameState.setPositionHash(positionReader.getAllPositions());
        gameState.setRepetitionCounter(repetitionCounter);
        gameState.setLegalMoves(legalMoves);
        gameState.setLegalMoves(status.isInProgress() ? legalMoves : new MoveContainer<>());
    }

    @Override
    public void notifyDoMove(Move move) {
        updateGameState();
    }

    @Override
    public void notifyUndoMove(Move move) {
    }

    protected AnalyzerResult analyze() {
        AnalyzerResult analyzerResult = new AnalyzerResult();

        pinnedAnalyzer.analyze(analyzerResult);

        kingSafePositionsAnalyzer.analyze(analyzerResult);

        return analyzerResult;
    }

    private Status calculateGameStatus(AnalyzerResult analysis, MoveContainerReader<Move> legalMoves, int repetitionCounter) {
        if (!legalMoves.isEmpty()) {
            if (fiftyMovesRule && positionReader.getHalfMoveClock() >= 100) {
                return Status.DRAW_BY_FIFTY_RULE;
            } else if (threefoldRepetitionRule && positionReader.getHalfMoveClock() >= 8) {
                if (repetitionCounter > 2) {
                    return Status.DRAW_BY_FOLD_REPETITION;
                }
            }
            return analysis.isKingInCheck() ? Status.CHECK : Status.NO_CHECK;
        }
        return analysis.isKingInCheck() ? Status.MATE : Status.STALEMATE;
    }

    private int calculateRepetitionCounter() {
        final long zobristHash = positionReader.getZobristHash();
        final long positionHash = positionReader.getAllPositions();

        int repetitionCounter = 1;
        int halfMoveClockCounter = positionReader.getHalfMoveClock();

        Iterator<GameHistoryRecord> gameStateHistoryIterator = careTaker.iterator();

        // Start iterating
        while (gameStateHistoryIterator.hasNext() && halfMoveClockCounter >= 0) {
            // Skip next state, opponent turn
            gameStateHistoryIterator.next();
            halfMoveClockCounter--;


            if (gameStateHistoryIterator.hasNext() && halfMoveClockCounter >= 0) {
                // Get next state, my turn
                GameHistoryRecord gameHistoryRecord = gameStateHistoryIterator.next();
                halfMoveClockCounter--;

                GameStateReader pastState = gameHistoryRecord.gameState();
                long pastZobristHash = gameHistoryRecord.zobristHash().getZobristHash();

                if (pastZobristHash == zobristHash && pastState.getPositionHash() == positionHash) {
                    repetitionCounter = pastState.getRepetitionCounter() + 1;
                    break;
                }
            }
        }

        return repetitionCounter;
    }
}
