package net.chesstango.board.position;

import net.chesstango.board.GameStatus;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.representations.fen.FEN;

/**
 * @author Mauricio Coria
 */
public interface GameStateReader {

    GameStatus getStatus();

    MoveContainerReader<? extends Move> getLegalMoves();

    Move getSelectedMove();

    AnalyzerResult getAnalyzerResult();

    long getZobristHash();

    long getPositionHash();

    int getRepetitionCounter();

    GameStateReader getPreviousState();
}
