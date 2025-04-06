package net.chesstango.board.position;

import net.chesstango.board.GameStatus;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;

/**
 * @author Mauricio Coria
 */
public interface GameStateReader {
    GameStatus getGameStatus();

    MoveContainerReader<Move> getLegalMoves();

    AnalyzerResult getAnalyzerResult();

    long getZobristHash();

    long getPositionHash();

    int getRepetitionCounter();
}
