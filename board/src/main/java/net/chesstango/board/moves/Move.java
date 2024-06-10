package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.legal.MoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public interface Move {
    PiecePositioned getFrom();

    PiecePositioned getTo();

    default void doMove(ChessPosition chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoard();
        BitBoardWriter bitBoard = chessPosition.getBitBoard();
        PositionStateWriter positionState = chessPosition.getPositionState();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCache();
        ZobristHashWriter hash = chessPosition.getZobrist();

        doMove(squareBoard);

        doMove(bitBoard);

        doMove(positionState);

        doMove(moveCache);

        doMove(hash, chessPosition);
    }

    default void undoMove(ChessPosition chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoard();
        BitBoardWriter bitBoard = chessPosition.getBitBoard();
        PositionStateWriter positionState = chessPosition.getPositionState();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCache();
        ZobristHashWriter hash = chessPosition.getZobrist();

        undoMove(squareBoard);

        undoMove(bitBoard);

        undoMove(positionState);

        undoMove(moveCache);

        undoMove(hash);
    }

    void doMove(SquareBoardWriter squareBoard);

    void undoMove(SquareBoardWriter squareBoard);

    void doMove(PositionStateWriter positionState);

    void undoMove(PositionStateWriter positionStateWriter);

    void doMove(BitBoardWriter bitBoard);

    void undoMove(BitBoardWriter bitBoard);

    void doMove(MoveCacheBoardWriter moveCache);

    void undoMove(MoveCacheBoardWriter moveCache);

    void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader);

    void undoMove(ZobristHashWriter hash);

    /**
     * "move" is a bit field with the following meaning (bit 0 is the least significant bit)
     * bits                meaning
     * ===================================
     * 0,1,2               to file
     * 3,4,5               to row
     * 6,7,8               from file
     * 9,10,11             from row
     * 12,13,14            promotion piece
     * <p>
     * "promotion piece" is encoded as follows
     * none       0
     * knight     1
     * bishop     2
     * rook       3
     * queen      4
     *
     * @return
     */
    default short binaryEncoding() {
        Square fromSquare = getFrom().getSquare();

        Square toSquare = getTo().getSquare();

        return (short) (fromSquare.getBinaryEncodedFrom() | toSquare.getBinaryEncodedTo());
    }

    Cardinal getMoveDirection();

    boolean isQuiet();
}