package net.chesstango.board.moves.imp;

import lombok.Setter;
import net.chesstango.board.GameImp;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
@Setter
public class MoveComposed extends MoveImp {

    private MoveExecutorLayer<PositionStateWriter> fnDoPositionState;

    private MoveExecutorLayer<SquareBoardWriter> fnDoSquareBoard;

    private MoveExecutorLayer<SquareBoardWriter> fnUndoSquareBoard;

    private MoveExecutorLayer<BitBoardWriter> fnDoColorBoard;

    private MoveExecutorLayer<BitBoardWriter> fnUndoColorBoard;

    private MoveExecutorZobrist fnDoZobrist;

    public MoveComposed(GameImp gameImp, PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(gameImp, from, to, direction);
    }

    public MoveComposed(GameImp gameImp, PiecePositioned from, PiecePositioned to) {
        super(gameImp, from, to);
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
    public void doMove(ZobristHashWriter hash) {
        fnDoZobrist.apply(from, to, hash, gameImp.getChessPosition());
    }

    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().getPiece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
    }
}
