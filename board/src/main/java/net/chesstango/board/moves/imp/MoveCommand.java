package net.chesstango.board.moves.imp;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public interface MoveCommand extends Move {

    void doMove(ChessPosition chessPosition);

    void undoMove(ChessPosition chessPosition);

    void doMove(SquareBoardWriter squareBoard);

    void undoMove(SquareBoardWriter squareBoard);

    void doMove(PositionStateWriter positionState);

    void doMove(BitBoardWriter bitBoard);

    void undoMove(BitBoardWriter bitBoard);

    void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader);

    void undoMove(PositionStateWriter positionStateWriter);

    void doMove(MoveCacheBoardWriter moveCache);

    void undoMove(MoveCacheBoardWriter moveCache);

    void doMove(KingSquareWriter kingSquareWriter);

    void undoMove(KingSquareWriter kingSquareWriter);

    void undoMove(ZobristHashWriter hash);

    boolean isLegalMove(LegalMoveFilter filter);
}
