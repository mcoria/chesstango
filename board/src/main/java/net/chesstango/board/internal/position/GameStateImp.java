package net.chesstango.board.internal.position;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Status;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.GameState;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.representations.fen.FEN;

/**
 * Implementation of the GameState interface that manages the state of a chess game.
 * This class provides methods to get and set various game state attributes such as
 * game status, legal moves, selected move, analyzer result, and hash values.
 * It also supports pushing and popping game states to handle state changes.
 * <p>
 * The GameStateData inner class holds the actual state data.
 *
 * @author Mauricio Coria
 */
@Setter
@Getter
public class GameStateImp implements GameState, Cloneable {
    private FEN initialFEN;

    private AnalyzerResult analyzerResult;
    private MoveContainerReader<Move> legalMoves;
    private Status status;
    private long positionHash;
    private int repetitionCounter;


    @Override
    public void reset() {
        analyzerResult = null;
        legalMoves = null;
        status = null;
        positionHash = 0L;
        repetitionCounter = 0;
    }

    @Override
    public GameStateReader takeSnapshot() {
        return this.clone();
    }

    @Override
    public void restoreSnapshot(GameStateReader snapshot) {
        analyzerResult = snapshot.getAnalyzerResult();
        legalMoves = snapshot.getLegalMoves();
        status = snapshot.getStatus();
        positionHash = snapshot.getPositionHash();
        repetitionCounter = snapshot.getRepetitionCounter();
    }


    @Override
    public GameStateImp clone() {
        GameStateImp gameState = new GameStateImp();
        gameState.setAnalyzerResult(analyzerResult);
        gameState.setLegalMoves(legalMoves);
        gameState.setStatus(status);
        gameState.setPositionHash(positionHash);
        gameState.setRepetitionCounter(repetitionCounter);
        return gameState;
    }
}