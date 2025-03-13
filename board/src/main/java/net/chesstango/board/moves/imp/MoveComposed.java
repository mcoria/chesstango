package net.chesstango.board.moves.imp;

import lombok.Setter;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
@Setter
public class MoveComposed extends MoveImp {

    private MoveLayerExecutor<PositionStateWriter> fnDoPositionState;

    private MoveLayerExecutor<SquareBoardWriter> fnDoSquareBoard;

    private MoveLayerExecutor<SquareBoardWriter> fnUndoSquareBoard;

    private MoveLayerExecutor<BitBoardWriter> fnDoColorBoard;

    private MoveLayerExecutor<BitBoardWriter> fnUndoColorBoard;

    private ZobristExecutor fnDoZobrist;

    public MoveComposed(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(from, to, direction);
    }

    public MoveComposed(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    @Override
    public void doMove(SquareBoardWriter squareBoard) {
        fnDoSquareBoard.apply(from, to, squareBoard);
    }

    @Override
    public void undoMove(SquareBoardWriter squareBoard) {
        fnUndoSquareBoard.apply(from, to, squareBoard);
    }

    @Override
    public void doMove(PositionStateWriter positionState) {
        fnDoPositionState.apply(from, to, positionState);
    }

    @Override
    public void doMove(BitBoardWriter bitBoard) {
        fnDoColorBoard.apply(from, to, bitBoard);
    }

    @Override
    public void undoMove(BitBoardWriter bitBoard) {
        fnUndoColorBoard.apply(from, to, bitBoard);
    }

    @Override
    public void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader) {
        fnDoZobrist.apply(from, to, hash, chessPositionReader);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoveComposed theOther) {
            return from.equals(theOther.from) && to.equals(theOther.to);
        }
        return false;
    }

    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().getPiece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
    }
}
