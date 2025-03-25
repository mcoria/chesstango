package net.chesstango.board.moves.imp;

import net.chesstango.board.GameStateWriter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public interface MoveCommand extends Move {

    void doMove(GameStateWriter gameState);
    void undoMove(GameStateWriter gameState);

    void doMove(ChessPositionWriter chessPosition);
    void undoMove(ChessPositionWriter chessPosition);

    void doMove(SquareBoardWriter squareBoard);
    void undoMove(SquareBoardWriter squareBoard);

    void doMove(PositionStateWriter positionState);
    void undoMove(PositionStateWriter positionState);

    void doMove(BitBoardWriter bitBoard);
    void undoMove(BitBoardWriter bitBoard);

    void doMove(MoveCacheBoardWriter moveCache);
    void undoMove(MoveCacheBoardWriter moveCache);

    void doMove(KingSquareWriter kingSquare);
    void undoMove(KingSquareWriter kingSquare);

    void doMove(ZobristHashWriter hash);
    void undoMove(ZobristHashWriter hash);

    boolean isLegalMove(LegalMoveFilter filter);
}
