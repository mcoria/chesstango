package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.BoardReader;
import net.chesstango.board.position.BoardWriter;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 *
 */
class MovePawnPromotion implements MovePromotion {
    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final Piece promotion;
    protected final Cardinal direction;

    private MoveExecutor<ColorBoard> fnDoColorBoard;
    private MoveExecutor<ColorBoard> fnUndoColorBoard;

    public MovePawnPromotion(PiecePositioned from, PiecePositioned to, Cardinal direction, Piece promotion) {
        this.from = from;
        this.to = to;
        this.direction = direction;
        this.promotion = promotion;
    }

    public MovePawnPromotion(PiecePositioned from, PiecePositioned to, Piece promotion) {
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
    public void executeMove(BoardWriter board) {
        board.setEmptyPosition(from);
        board.setPiece(to.getSquare(), this.promotion);
    }

    @Override
    public void undoMove(BoardWriter board) {
        board.setPosition(from);
        board.setPosition(to);
    }

    @Override
    public void executeMove(PositionState positionState) {
        positionState.pushState();
        positionState.resetHalfMoveClock();
        positionState.setEnPassantSquare(null);

        // Captura
        if(to != null) {
            if (MoveCastlingWhiteKing.ROOK_FROM.equals(to)) {
                positionState.setCastlingWhiteKingAllowed(false);
            }

            if (MoveCastlingWhiteQueen.ROOK_FROM.equals(to)) {
                positionState.setCastlingWhiteQueenAllowed(false);
            }

            if (MoveCastlingBlackKing.ROOK_FROM.equals(to)) {
                positionState.setCastlingBlackKingAllowed(false);
            }

            if (MoveCastlingBlackQueen.ROOK_FROM.equals(to)) {
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
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, BoardReader board) {
        hash.pushState();

        hash.xorPosition(from);

        if(to.getPiece() != null) {
            hash.xorPosition(to);
        }

        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), promotion));

        if(oldPositionState.isCastlingWhiteKingAllowed() != newPositionState.isCastlingWhiteKingAllowed()){
            hash.xorCastleWhiteKing();
        }

        if(oldPositionState.isCastlingWhiteQueenAllowed() != newPositionState.isCastlingWhiteQueenAllowed()){
            hash.xorCastleWhiteQueen();
        }


        if(oldPositionState.isCastlingBlackKingAllowed() != newPositionState.isCastlingBlackKingAllowed()){
            hash.xorCastleBlackKing();
        }

        if(oldPositionState.isCastlingBlackQueenAllowed() != newPositionState.isCastlingBlackQueenAllowed()){
            hash.xorCastleBlackQueen();
        }

        hash.xorOldEnPassantSquare();

        hash.xorTurn();
    }

    @Override
    public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, BoardReader board) {
        hash.popState();
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
        if(obj instanceof MovePawnPromotion){
            MovePawnPromotion theOther = (MovePawnPromotion) obj;
            return from.equals(theOther.from) &&  to.equals(theOther.to) && promotion.equals(theOther.promotion);
        }
        return false;
    }

    @Override
    public short binaryEncoding() {
        short fromToEncoded =  MovePromotion.super.binaryEncoding();
        short pieceEncoded = switch (promotion) {
            case KNIGHT_BLACK, KNIGHT_WHITE -> 1;
            case BISHOP_BLACK, BISHOP_WHITE -> 2;
            case ROOK_BLACK, ROOK_WHITE -> 3;
            case QUEEN_BLACK, QUEEN_WHITE -> 4;
            default -> throw new RuntimeException("Invalid promotion");
        };
        return (short) (pieceEncoded << 12 | fromToEncoded);
    }

    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().getPiece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
    }
}
