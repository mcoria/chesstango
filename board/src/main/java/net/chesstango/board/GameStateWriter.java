package net.chesstango.board;

import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;


/**
 * @author Mauricio Coria
 */
public interface GameStateWriter {
    void setStatus(GameStatus gameStatus);

    void setLegalMoves(MoveContainerReader legalMoves);

    void setSelectedMove(Move selectedMove);

    void setAnalyzerResult(AnalyzerResult analyzerResult);

    void setZobristHash(long zobristHash);

    void setPositionHash(long positionHash);

    void setRepetitionCounter(int repetitionCounter);
}
