package net.chesstango.board.analyzer;

import net.chesstango.board.GameState;
import net.chesstango.board.GameStatus;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.position.ChessPositionReader;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mauricio Coria
 * <p>
 * Necesitamos los estadios para seleccionar el LegalMoveGenerator que corresponde
 * <p>
 * TODO: La generacion de movimientos dummy debiera ser en base al layer de color.
 * Me imagino un tablero con X y O para representar los distintos colores.
 */
public class PositionAnalyzer {
    private Analyzer pinnedAnalyzer;
    private Analyzer kingSafePositionsAnalyzer;
    private GameState gameState;
    private ChessPositionReader positionReader;
    private LegalMoveGenerator legalMoveGenerator;
    private boolean detectRepetitions;


    public void updateGameState() {
        AnalyzerResult analysis = analyze();

        MoveContainerReader legalMoves = legalMoveGenerator.getLegalMoves(analysis);

        boolean existsLegalMove = !legalMoves.isEmpty();

        GameStatus gameStatus = null;

        if (existsLegalMove) {
            if (positionReader.getHalfMoveClock() < 100) {
                if (analysis.isKingInCheck()) {
                    gameStatus = GameStatus.CHECK;
                } else {
                    gameStatus = GameStatus.NO_CHECK;
                }
            } else {
                gameStatus = GameStatus.DRAW_BY_FIFTY_RULE;
            }

            if (detectRepetitions) {
                final long currentHash = positionReader.getZobristHash();

                AtomicInteger repetitionCounter = new AtomicInteger();

                gameState.accept(visitedGameState -> {
                    if (currentHash == visitedGameState.getZobristHash()) {
                        repetitionCounter.incrementAndGet();
                    }
                });

                if (repetitionCounter.get() == 2) {
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
        gameState.setZobristHash(positionReader.getZobristHash());

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

    public void detectRepetitions(boolean flag) {
        this.detectRepetitions = flag;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setLegalMoveGenerator(LegalMoveGenerator legalMoveGenerator) {
        this.legalMoveGenerator = legalMoveGenerator;
    }

    public void setPositionReader(ChessPositionReader positionReader) {
        this.positionReader = positionReader;
    }

    public void setPinnedAnalyzer(Analyzer pinnedAnalyzer) {
        this.pinnedAnalyzer = pinnedAnalyzer;
    }

    public void setKingSafePositionsAnalyzer(Analyzer kingSafePositionsAnalyzer) {
        this.kingSafePositionsAnalyzer = kingSafePositionsAnalyzer;
    }
}
