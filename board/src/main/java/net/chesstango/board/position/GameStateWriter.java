package net.chesstango.board.position;

import net.chesstango.board.GameStatus;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.MoveCommand;


/**
 * @author Mauricio Coria
 */
public interface GameStateWriter {
    void setStatus(GameStatus gameStatus);

    void setLegalMoves(MoveContainerReader<MoveCommand> legalMoves);

    void setSelectedMove(Move selectedMove);

    void setAnalyzerResult(AnalyzerResult analyzerResult);

    void setZobristHash(long zobristHash);

    void setPositionHash(long positionHash);

    void setRepetitionCounter(int repetitionCounter);

    void push();

    void pop();
}
