package net.chesstango.board.analyzer;

import lombok.Setter;
import net.chesstango.board.GameState;
import net.chesstango.board.GameStateReader;
import net.chesstango.board.GameStatus;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 * <p>
 * Necesitamos los estadios para seleccionar el LegalMoveGenerator que corresponde
 * <p>
 * TODO: La generacion de movimientos dummy debiera ser en base al layer de color.
 * Me imagino un tablero con X y O para representar los distintos colores.
 */
public class PositionAnalyzer {
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
    private boolean threefoldRepetitionRule;
    private boolean fiftyMovesRule;


    public void updateGameState() {
        AnalyzerResult analysis = analyze();

        MoveContainerReader legalMoves = legalMoveGenerator.getLegalMoves(analysis);

        boolean existsLegalMove = !legalMoves.isEmpty();

        GameStatus gameStatus = null;

        int repetitionCounter = getRepetitionCounter();

        if (existsLegalMove) {
            if (fiftyMovesRule && positionReader.getHalfMoveClock() >= 100) {
                gameStatus = GameStatus.DRAW_BY_FIFTY_RULE;
            } else if (threefoldRepetitionRule && positionReader.getHalfMoveClock() >= 8) {
                if (repetitionCounter > 2) {
                    gameStatus = GameStatus.DRAW_BY_FOLD_REPETITION;
                }
            }

            if (gameStatus == null) {
                if (analysis.isKingInCheck()) {
                    gameStatus = GameStatus.CHECK;
                } else {
                    gameStatus = GameStatus.NO_CHECK;
                }
            }
        } else {
            if (analysis.isKingInCheck()) {
                gameStatus = GameStatus.MATE;
            } else {
                gameStatus = GameStatus.STALEMATE;
            }
        }

        gameState.setStatus(gameStatus);
        gameState.setAnalyzerResult(analysis);
        gameState.setZobristHash(positionReader.getZobristHash());
        gameState.setPositionHash(positionReader.getAllPositions());
        gameState.setRepetitionCounter(repetitionCounter);

        if (gameStatus.isFinalStatus()) {
            gameState.setLegalMoves(new MoveContainer());
        } else {
            gameState.setLegalMoves(legalMoves);
        }

    }

    public AnalyzerResult analyze() {

        AnalyzerResult result = new AnalyzerResult();

        pinnedAnalyzer.analyze(result);

        kingSafePositionsAnalyzer.analyze(result);

        return result;
    }

    public void threefoldRepetitionRule(boolean flag) {
        this.threefoldRepetitionRule = flag;
    }

    public void fiftyMovesRule(boolean flag) {
        this.fiftyMovesRule = flag;
    }


    private int getRepetitionCounter() {
        final long zobristHash = positionReader.getZobristHash();
        final long positionHash = positionReader.getAllPositions();

        int repetitionCounter = 1;

        GameStateReader currentState = gameState.getPreviousState();
        if (currentState != null) {
            currentState = currentState.getPreviousState();
        }

        int halfMoveClockCounter = positionReader.getHalfMoveClock();
        halfMoveClockCounter -= 2;

        while (currentState != null && halfMoveClockCounter >= 0) {

            if (zobristHash == currentState.getZobristHash() && positionHash == currentState.getPositionHash()) {
                repetitionCounter = currentState.getRepetitionCounter() + 1;
                break;
            }

            currentState = currentState.getPreviousState();
            if (currentState != null) {
                currentState = currentState.getPreviousState();
            }
            halfMoveClockCounter -= 2;
        }

        return repetitionCounter;
    }
}
