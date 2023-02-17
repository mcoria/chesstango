package net.chesstango.board.analyzer;

import net.chesstango.board.GameState;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.position.imp.PositionState;

import java.util.Iterator;

/*
 * Necesitamos los estadios para seleccionar el LegalMoveGenerator que corresponde
 */

//TODO: La generacion de movimientos dummy debiera ser en base al layer de color. 
//      Me imagino un tablero con X y O para representar los distintos colores.

/**
 * @author Mauricio Coria
 */
public class PositionAnalyzer {

    private CheckAndPinnedAnalyzer checkAndPinnedAnalyzer;

    private GameState gameState;

    private PositionState positionState;

    private LegalMoveGenerator legalMoveGenerator;
    private boolean detectRepetitions;


    public GameState.Status updateGameState() {
        AnalyzerResult analysis = analyze();

        MoveContainerReader legalMoves = legalMoveGenerator.getLegalMoves(analysis);

        boolean existsLegalMove = !legalMoves.isEmpty();

        GameState.Status status = null;

        if (existsLegalMove) {
            if (positionState.getHalfMoveClock() < 50) {
                if (analysis.isKingInCheck()) {
                    status = GameState.Status.CHECK;
                } else {
                    status = GameState.Status.NO_CHECK;
                }
            } else {
                status = GameState.Status.DRAW_BY_FIFTY_RULE;
            }

            if (detectRepetitions) {
                final String currentFen = gameState.getFenWithoutClocks();
                int repetitionCounter = 0;
                Iterator<GameState.GameStateData> stateDataIterator = gameState.iterateGameStates();
                while (stateDataIterator.hasNext()) {
                    GameState.GameStateData stateData = stateDataIterator.next();
                    if (currentFen.equals(stateData.fenWithoutClocks)) {
                        repetitionCounter++;
                    }
                }
                if (repetitionCounter > 2) {
                    status = GameState.Status.DRAW_BY_FOLD_REPETITION;
                }
            }

        } else {
            if (analysis.isKingInCheck()) {
                status = GameState.Status.MATE;
            } else {
                status = GameState.Status.DRAW;
            }
        }

        gameState.setStatus(status);
        gameState.setAnalyzerResult(analysis);
        gameState.setLegalMoves(legalMoves);

        return status;
    }

    public AnalyzerResult analyze() {

        AnalyzerResult result = new AnalyzerResult();

        checkAndPinnedAnalyzer.analyze();

        result.setKingInCheck(checkAndPinnedAnalyzer.isKingInCheck());

        result.setPinnedSquares(checkAndPinnedAnalyzer.getPinnedPositions());

        result.setPinnedPositionCardinals(checkAndPinnedAnalyzer.getPinnedPositionCardinals());

        return result;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setLegalMoveGenerator(LegalMoveGenerator legalMoveGenerator) {
        this.legalMoveGenerator = legalMoveGenerator;
    }

    public void setCheckAndPinnedAnalyzer(CheckAndPinnedAnalyzer checkAndPinnedAnalyzer) {
        this.checkAndPinnedAnalyzer = checkAndPinnedAnalyzer;
    }

    public void setPositionState(PositionState positionState) {
        this.positionState = positionState;
    }

    public void detectRepetitions(boolean flag) {
        this.detectRepetitions = flag;
    }
}
