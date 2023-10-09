package net.chesstango.board;

import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;

/**
 * @author Mauricio Coria
 */
public interface GameStateReader {
    GameStatus getStatus();

    MoveContainerReader getLegalMoves();

    Move getSelectedMove();

    AnalyzerResult getAnalyzerResult();

    long getZobristHash();

    GameStateReader getPreviousState();
}
