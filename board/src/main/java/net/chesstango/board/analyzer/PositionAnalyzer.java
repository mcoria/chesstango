package net.chesstango.board.analyzer;

import net.chesstango.board.GameState;
import net.chesstango.board.GameStateReader;
import net.chesstango.board.GameStatus;
import net.chesstango.board.GameVisitor;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.position.ChessPositionReader;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *  @author Mauricio Coria
 *
 * Necesitamos los estadios para seleccionar el LegalMoveGenerator que corresponde
 *
 * TODO: La generacion de movimientos dummy debiera ser en base al layer de color.
 * Me imagino un tablero con X y O para representar los distintos colores.
 *
 *
 */
public class PositionAnalyzer {
    private Analyzer checkAnalyzer;
    private Analyzer capturedPositionsAnalyzer;
    private Analyzer pinnedAnalyzer;
    private GameState gameState;
    private ChessPositionReader positionReader;
    private LegalMoveGenerator legalMoveGenerator;
    private boolean detectRepetitions;


    public GameStatus updateGameState() {
        AnalyzerResult analysis = analyze();

        MoveContainerReader legalMoves = legalMoveGenerator.getLegalMoves(analysis);

        boolean existsLegalMove = !legalMoves.isEmpty();

        GameStatus gameStatus = null;

        if (existsLegalMove) {
            if (positionReader.getHalfMoveClock() < 50) {
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

                gameState.accept(new GameVisitor() {
                    @Override
                    public void visit(GameStateReader gameState) {
                        if (currentFen.equals(gameState.getFenWithoutClocks())) {
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

        checkAnalyzer.analyze(result);

        pinnedAnalyzer.analyze(result);

        //capturedPositionsAnalyzer.analyze(result);

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

    public void setCheckAnalyzer(CheckAnalyzer checkAnalyzer) {
        this.checkAnalyzer = checkAnalyzer;
    }
    public void setCapturedPositionsAnalyzer(Analyzer capturedPositionsAnalyzer) {
        this.capturedPositionsAnalyzer = capturedPositionsAnalyzer;
    }

    public void setPinnedAnalyzer(Analyzer pinnedAnalyzer) {
        this.pinnedAnalyzer = pinnedAnalyzer;
    }
}
