package net.chesstango.board.moves.imp;

import net.chesstango.board.Color;
import net.chesstango.board.GameImp;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveCaptureEnPassant;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public class MoveCaptureEnPassantImp extends MoveImp implements MoveCaptureEnPassant {
    protected final PiecePositioned capture;


    public MoveCaptureEnPassantImp(GameImp gameImp, PiecePositioned from, PiecePositioned to, Cardinal direction, PiecePositioned capture) {
        super(gameImp, from, to, direction);
        this.capture = capture;
    }

    @Override
    public void doMove(SquareBoardWriter squareBoard) {
        squareBoard.move(from, to);
        squareBoard.setEmptyPosition(capture);
    }

    @Override
    public void undoMove(SquareBoardWriter squareBoard) {
        squareBoard.setPosition(from);
        squareBoard.setPosition(to);
        squareBoard.setPosition(capture);
    }

    @Override
    public void doMove(PositionStateWriter positionState) {
        positionState.pushState();

        positionState.setEnPassantSquare(null);


        positionState.resetHalfMoveClock();

        if (Color.BLACK.equals(from.getPiece().getColor())) {
            positionState.incrementFullMoveClock();
        }

        positionState.rollTurn();
    }

    @Override
    public void doMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), from.getSquare(), to.getSquare());

        bitBoardWriter.removePosition(capture);
    }

    @Override
    public void undoMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), to.getSquare(), from.getSquare());

        bitBoardWriter.addPosition(capture);
    }

    @Override
    public void doMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare(), capture.getSquare());
        moveCache.push();
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare(), capture.getSquare());
        moveCache.pop();
    }

    @Override
    public void doMove(KingSquareWriter kingSquare) {

    }

    @Override
    public void undoMove(KingSquareWriter kingSquare) {

    }

    @Override
    public void doMove(ZobristHashWriter hash) {
        hash.pushState();

        hash.xorPosition(from);

        hash.xorPosition(capture);

        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));

        hash.clearEnPassantSquare();

        hash.xorTurn();
    }

    @Override
    public Cardinal getMoveDirection() {
        return direction;
    }

    @Override
    public boolean isQuiet() {
        return false;
    }

    @Override
    public PiecePositioned getCapture() {
        return capture;
    }

    @Override
    public boolean isLegalMove(LegalMoveFilter filter) {
        return filter.isLegalMovePawn(this, this);
    }
}
