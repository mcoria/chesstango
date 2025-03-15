package net.chesstango.board.moves.imp;

import net.chesstango.board.Color;
import net.chesstango.board.GameImp;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public class MovePromotionImp extends MoveImp implements MovePromotion {
    protected final Piece promotion;

    public MovePromotionImp(GameImp gameImp, PiecePositioned from, PiecePositioned to, Cardinal direction, Piece promotion) {
        super(gameImp, from, to, direction);
        this.promotion = promotion;
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
    public void doMove(SquareBoardWriter squareBoard) {
        squareBoard.setEmptyPosition(from);
        squareBoard.setPiece(to.getSquare(), this.promotion);
    }

    @Override
    public void undoMove(SquareBoardWriter squareBoard) {
        squareBoard.setPosition(from);
        squareBoard.setPosition(to);
    }

    @Override
    public void doMove(PositionStateWriter positionState) {
        positionState.pushState();
        positionState.resetHalfMoveClock();
        positionState.setEnPassantSquare(null);

        // Captura
        if (to != null) {
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

        if (Color.BLACK.equals(from.getPiece().getColor())) {
            positionState.incrementFullMoveClock();
        }

        positionState.rollTurn();
    }

    @Override
    public void undoMove(PositionStateWriter positionStateWriter) {
        positionStateWriter.popState();
    }

    @Override
    public void doMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.removePosition(from);
        // Captura
        if (to.getPiece() != null) {
            bitBoardWriter.removePosition(to);
        }
        bitBoardWriter.addPosition(promotion, to.getSquare());
    }

    @Override
    public void undoMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.removePosition(promotion, to.getSquare());
        // Captura
        if (to.getPiece() != null) {
            bitBoardWriter.addPosition(to);
        }
        bitBoardWriter.addPosition(from);
    }


    @Override
    public void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader) {
        hash.pushState();

        hash.xorPosition(from);

        if (to.getPiece() != null) {
            hash.xorPosition(to);
        }

        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), promotion));

        PositionStateReader oldPositionState = chessPositionReader.getPreviousPositionState();

        if (oldPositionState.isCastlingWhiteKingAllowed() != chessPositionReader.isCastlingWhiteKingAllowed()) {
            hash.xorCastleWhiteKing();
        }

        if (oldPositionState.isCastlingWhiteQueenAllowed() != chessPositionReader.isCastlingWhiteQueenAllowed()) {
            hash.xorCastleWhiteQueen();
        }


        if (oldPositionState.isCastlingBlackKingAllowed() != chessPositionReader.isCastlingBlackKingAllowed()) {
            hash.xorCastleBlackKing();
        }

        if (oldPositionState.isCastlingBlackQueenAllowed() != chessPositionReader.isCastlingBlackQueenAllowed()) {
            hash.xorCastleBlackQueen();
        }

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
    public Piece getPromotion() {
        return promotion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MovePromotionImp theOther) {
            return from.equals(theOther.from) && to.equals(theOther.to) && promotion.equals(theOther.promotion);
        }
        return false;
    }

    @Override
    public short binaryEncoding() {
        short fromToEncoded = MovePromotion.super.binaryEncoding();
        short pieceEncoded = switch (promotion) {
            case KNIGHT_BLACK, KNIGHT_WHITE -> 1;
            case BISHOP_BLACK, BISHOP_WHITE -> 2;
            case ROOK_BLACK, ROOK_WHITE -> 3;
            case QUEEN_BLACK, QUEEN_WHITE -> 4;
            default -> throw new RuntimeException("Invalid promotion");
        };
        return (short) (pieceEncoded << 12 | fromToEncoded);
    }
}
