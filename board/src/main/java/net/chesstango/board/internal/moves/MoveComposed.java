package net.chesstango.board.internal.moves;

import lombok.Setter;
import net.chesstango.board.internal.GameImp;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.*;

import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
@Setter
public class MoveComposed extends MoveImp {

    private transient MoveExecutorLayer<PositionStateWriter> fnDoPositionState;

    private transient MoveExecutorLayer<SquareBoardWriter> fnDoSquareBoard;

    private transient MoveExecutorLayer<SquareBoardWriter> fnUndoSquareBoard;

    private transient MoveExecutorLayer<BitBoardWriter> fnDoBitBoard;

    private transient MoveExecutorLayer<BitBoardWriter> fnUndoBitBoard;

    private transient MoveExecutorZobrist fnDoZobrist;

    private transient Predicate<LegalMoveFilter> fnDoFilterMove;

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
    public void doMove(PositionState positionState) {
        positionStateSnapshot = positionState.takeSnapshot();

        fnDoPositionState.apply(from, to, positionState);
    }

    @Override
    public void doMove(BitBoardWriter bitBoard) {
        fnDoBitBoard.apply(from, to, bitBoard);
    }

    @Override
    public void undoMove(BitBoardWriter bitBoard) {
        fnUndoBitBoard.apply(from, to, bitBoard);
    }

    @Override
    public void doMove(ZobristHash hash) {
        zobristHashSnapshot = hash.takeSnapshot();
        fnDoZobrist.apply(from, to, hash, gameImp.getPosition(), positionStateSnapshot);
    }

    @Override
    public void doMove(KingSquareWriter kingSquare) {
    }

    @Override
    public void undoMove(KingSquareWriter kingSquare) {
    }

    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().piece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().square(), getTo().square());
    }

    @Override
    public boolean isLegalMove(LegalMoveFilter filter) {
        return fnDoFilterMove.test(filter);
    }
}
