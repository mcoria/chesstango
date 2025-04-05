package net.chesstango.board.analyzer;

import lombok.Setter;
import net.chesstango.board.GameListener;
import net.chesstango.board.GameStatus;
import net.chesstango.board.iterators.state.LastToFirst;
import net.chesstango.board.iterators.state.StateIterator;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.generators.legal.LegalMoveGenerator;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.GameState;
import net.chesstango.board.position.GameStateReader;

/**
 * @author Mauricio Coria
 * <p>
 * Necesitamos los estadios para seleccionar el LegalMoveGenerator que corresponde
 * <p>
 * TODO: La generacion de movimientos dummy debiera ser en base al layer de color.
 * Me imagino un tablero con X y O para representar los distintos colores.
 */
public class PositionAnalyzer implements GameListener {
    @Setter
    private Analyzer pinnedAnalyzer;

    @Setter
    private Analyzer kingSafePositionsAnalyzer;

    @Setter
    private GameState gameState;

    @Setter
    private ChessPositionReader positionReader;

    @Setter
    private LegalMoveGenerator legalMoveGenerator;

    @Setter
    private boolean threefoldRepetitionRule;

    @Setter
    private boolean fiftyMovesRule;

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

    public AnalyzerResult analyze() {

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


        StateIterator gameStateIterator = new LastToFirst(gameState);

        // Skip the first state, initial position
        if (gameStateIterator.hasNext()) {
            // Skip the current state, my turn
            gameStateIterator.next();
            halfMoveClockCounter--;


            if (gameStateIterator.hasNext()) {
                // Skip next state, opponent turn
                gameStateIterator.next();
                halfMoveClockCounter--;

                // Start iterating
                while (gameStateIterator.hasNext() && halfMoveClockCounter >= 0) {

                    // Get the current state
                    GameStateReader currentState = gameStateIterator.next();
                    halfMoveClockCounter--;

                    if (currentState.getZobristHash() == zobristHash && currentState.getPositionHash() == positionHash) {
                        repetitionCounter = currentState.getRepetitionCounter() + 1;
                        break;
                    }

                    // Skip next state, opponent turn
                    if (gameStateIterator.hasNext()) {
                        gameStateIterator.next();
                        halfMoveClockCounter -= 1;
                    }
                }
            }
        }


        return repetitionCounter;
    }
}
