package net.chesstango.board.position;

import net.chesstango.board.Status;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;


/**
 * @author Mauricio Coria
 */
public interface GameStateWriter {

    void setStatus(Status status);

    void setLegalMoves(MoveContainerReader<Move> legalMoves);

    void setAnalyzerResult(AnalyzerResult analyzerResult);

    void setZobristHash(long zobristHash);

    void setPositionHash(long positionHash);

    void setRepetitionCounter(int repetitionCounter);
}
