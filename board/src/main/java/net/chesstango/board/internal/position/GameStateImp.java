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

    private AnalyzerResult analyzerResult;
    private MoveContainerReader<Move> legalMoves;
    private Status status;
    private long zobristHash;
    private long positionHash;
    private int repetitionCounter;
    private FEN initialFEN;


    @Override
    public GameStateReader takeSnapshot() {
        return this.clone();
    }

    @Override
    public void restoreSnapshot(GameStateReader snapshot) {
        this.analyzerResult = snapshot.getAnalyzerResult();
        this.legalMoves = snapshot.getLegalMoves();
        this.status = snapshot.getStatus();
        this.zobristHash = snapshot.getZobristHash();
        this.positionHash = snapshot.getPositionHash();
        this.repetitionCounter = snapshot.getRepetitionCounter();
    }


    @Override
    public GameStateImp clone() {
        GameStateImp gameState = new GameStateImp();
        gameState.setAnalyzerResult(this.analyzerResult);
        gameState.setLegalMoves(this.legalMoves);
        gameState.setStatus(this.status);
        gameState.setZobristHash(this.zobristHash);
        gameState.setPositionHash(this.positionHash);
        gameState.setRepetitionCounter(this.repetitionCounter);
        return gameState;
    }
}