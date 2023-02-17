package net.chesstango.board.analyzer;

import net.chesstango.board.GameState;
import net.chesstango.board.GameStateVisitor;
import net.chesstango.board.GameStatus;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.position.imp.PositionState;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

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


    public GameStatus updateGameState() {
        AnalyzerResult analysis = analyze();

        MoveContainerReader legalMoves = legalMoveGenerator.getLegalMoves(analysis);

        boolean existsLegalMove = !legalMoves.isEmpty();

        GameStatus gameStatus = null;

        if (existsLegalMove) {
            if (positionState.getHalfMoveClock() < 50) {
                if (analysis.isKingInCheck()) {
                    gameStatus = GameStatus.CHECK;
                } else {
                    gameStatus = GameStatus.NO_CHECK;
                }
            } else {
                gameStatus = GameStatus.DRAW_BY_FIFTY_RULE;
            }

            if (detectRepetitions) {
                final String currentFen = gameState.getFenWithoutClocks();

                AtomicInteger repetitionCounter = new AtomicInteger();

                gameState.accept(new GameStateVisitor() {
                    @Override
                    public void visit(GameState gameState) {
                    }

                    @Override
                    public void visit(GameState.GameStateData gameStateData) {
                        if (currentFen.equals(gameStateData.fenWithoutClocks)) {
                            repetitionCounter.incrementAndGet();
                        }
                    }
                });

                if (repetitionCounter.get() > 2) {
                    gameStatus = GameStatus.DRAW_BY_FOLD_REPETITION;
                }
            }

        } else {
            if (analysis.isKingInCheck()) {
                gameStatus = GameStatus.MATE;
            } else {
                gameStatus = GameStatus.DRAW;
            }
        }

        gameState.setStatus(gameStatus);
        gameState.setAnalyzerResult(analysis);
        gameState.setLegalMoves(legalMoves);

        return gameStatus;
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
