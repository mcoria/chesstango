package net.chesstango.board.moves.bridge;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.PiecePlacementWriter;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 *
 */
public class MovePromotionImp implements MovePromotion {

    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final Piece promotion;
    protected final Cardinal direction;

    private MoveExecutor<ColorBoard> fnDoColorBoard;
    private MoveExecutor<ColorBoard> fnUndoColorBoard;

    public MovePromotionImp(PiecePositioned from, PiecePositioned to, Cardinal direction, Piece promotion) {
        this.from = from;
        this.to = to;
        this.direction = direction;
        this.promotion = promotion;
    }

    public MovePromotionImp(PiecePositioned from, PiecePositioned to, Piece promotion) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
        this.direction =  calculateMoveDirection();
    }

    @Override
    public PiecePositioned getFrom() {
        return from;
    }

    @Override
    public PiecePositioned getTo() {
        return to;
    }


    @Override
    public void executeMove(PiecePlacementWriter board) {
        board.setEmptyPosicion(from);
        board.setPieza(to.getSquare(), this.promotion);
    }

    @Override
    public void undoMove(PiecePlacementWriter board) {
        board.setPosicion(from);
        board.setPosicion(to);
    }

    @Override
    public void executeMove(PositionState positionState) {
        positionState.pushState();
        positionState.resetHalfMoveClock();
        positionState.setEnPassantSquare(null);

        // Captura
        if(to != null) {
            if (CastlingWhiteKingMove.ROOK_FROM.equals(to)) {
                positionState.setCastlingWhiteKingAllowed(false);
            }

            if (CastlingWhiteQueenMove.ROOK_FROM.equals(to)) {
                positionState.setCastlingWhiteQueenAllowed(false);
            }

            if (CastlingBlackKingMove.ROOK_FROM.equals(to)) {
                positionState.setCastlingBlackKingAllowed(false);
            }

            if (CastlingBlackQueenMove.ROOK_FROM.equals(to)) {
                positionState.setCastlingBlackQueenAllowed(false);
            }
        }

        if(Color.BLACK.equals(positionState.getCurrentTurn())){
            positionState.incrementFullMoveClock();
        }

        positionState.rollTurn();
    }

    @Override
    public void undoMove(PositionState positionState) {
        positionState.popState();
    }

    @Override
    public void executeMove(ColorBoard colorBoard) {
        fnDoColorBoard.apply(from, to, colorBoard);
    }

    @Override
    public void undoMove(ColorBoard colorBoard) {
        fnUndoColorBoard.apply(from, to, colorBoard);
    }


    @Override
    public void executeMove(MoveCacheBoard moveCache) {
        moveCache.pushCleared();
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), true);
    }

    @Override
    public void undoMove(MoveCacheBoard moveCache) {
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), false);
        moveCache.popCleared();
    }

    @Override
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {

    }

    @Override
    public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {

    }

    @Override
    public Cardinal getMoveDirection() {
        return direction;
    }

    @Override
    public Piece getPromotion() {
        return promotion;
    }

    public void setFnDoColorBoard(MoveExecutor<ColorBoard> fnDoColorBoard) {
        this.fnDoColorBoard = fnDoColorBoard;
    }

    public void setFnUndoColorBoard(MoveExecutor<ColorBoard> fnUndoColorBoard) {
        this.fnUndoColorBoard = fnUndoColorBoard;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MovePromotionImp){
            MovePromotionImp theOther = (MovePromotionImp) obj;
            return from.equals(theOther.from) &&  to.equals(theOther.to) && promotion.equals(theOther.promotion);
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
