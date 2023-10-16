package net.chesstango.board.analyzer;

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
    private Analyzer pinnedAnalyzer;
    private Analyzer kingSafePositionsAnalyzer;
    private GameState gameState;
    private ChessPositionReader positionReader;
    private LegalMoveGenerator legalMoveGenerator;
    private boolean threefoldRepetitionRule;
    private boolean fiftyMovesRule;


    public void updateGameState() {
        AnalyzerResult analysis = analyze();

        MoveContainerReader legalMoves = legalMoveGenerator.getLegalMoves(analysis);

        boolean existsLegalMove = !legalMoves.isEmpty();

        GameStatus gameStatus = null;

        if (existsLegalMove) {
            if (fiftyMovesRule && positionReader.getHalfMoveClock() >= 100) {
                gameStatus = GameStatus.DRAW_BY_FIFTY_RULE;
            } else if (threefoldRepetitionRule && positionReader.getHalfMoveClock() >= 8) {
                int repetitionCounter = getRepetitionCounter();
                if (repetitionCounter >= 3) {
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


    private int getRepetitionCounter() {
        final long zobristHash = positionReader.getZobristHash();
        final long positionHash = positionReader.getAllPositions();

        int repetitionCounter = 1;
        int counter = positionReader.getHalfMoveClock();

        GameStateReader currentState = gameState.getPreviousState();
        while (currentState != null && counter > 0) {
            if (zobristHash == currentState.getZobristHash() &&
                    positionHash == currentState.getPositionHash()) {
                repetitionCounter++;
            }
            currentState = currentState.getPreviousState();
            counter--;
        }
        return repetitionCounter;
    }
}
