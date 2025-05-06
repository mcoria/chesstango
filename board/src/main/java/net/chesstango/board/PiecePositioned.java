package net.chesstango.board;

import lombok.Getter;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
@Getter
public final class PiecePositioned {
    private final Square square;
    private final Piece piece;

    public static final PiecePositioned ROOK_BLACK_QUEEN = PiecePositionedCache.getInstance().getPiecePositioned(Square.a8, Piece.ROOK_BLACK);
    public static final PiecePositioned ROOK_BLACK_KING = PiecePositionedCache.getInstance().getPiecePositioned(Square.h8, Piece.ROOK_BLACK);
    public static final PiecePositioned KING_BLACK = PiecePositionedCache.getInstance().getPiecePositioned(Square.e8, Piece.KING_BLACK);

    public static final PiecePositioned ROOK_WHITE_QUEEN = PiecePositionedCache.getInstance().getPiecePositioned(Square.a1, Piece.ROOK_WHITE);
    public static final PiecePositioned ROOK_WHITE_KING = PiecePositionedCache.getInstance().getPiecePositioned(Square.h1, Piece.ROOK_WHITE);
    public static final PiecePositioned KING_WHITE = PiecePositionedCache.getInstance().getPiecePositioned(Square.e1, Piece.KING_WHITE);

    private PiecePositioned(Square square, Piece piece) {
        this.square = square;
        this.piece = piece;
    }

    public static PiecePositioned of(Square square, Piece piece) {
        return PiecePositionedCache.getInstance().getPiecePositioned(square, piece);
    }

    public static PiecePositioned getPosition(Square square) {
        return PiecePositionedCache.getInstance().getPosition(square);
    }


    @Override
    public String toString() {
        return String.format("%s=%s", square, piece);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PiecePositioned that = (PiecePositioned) o;
        return square == that.square && piece == that.piece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(square, piece);
    }

    public PiecePositioned getMirrorPosition() {
        if(piece == null){
            return getPosition(square.mirror());
        }
        return of(square.mirror(), piece.getOpposite());
    }

    private static class PiecePositionedCache {

        private static final PiecePositionedCache theInstance = new PiecePositionedCache();
        private final PiecePositioned[][] board = new PiecePositioned[64][13];

        private PiecePositionedCache() {
            for (int file = 0; file < 8; file++) {
                for (int rank = 0; rank < 8; rank++) {
                    for (Piece piece : Piece.values()) {
                        board[Square.of(file, rank).idx()][piece.ordinal()] = new PiecePositioned(
                                Square.of(file, rank), piece);
                    }
                    board[Square.of(file, rank).idx()][12] = new PiecePositioned(
                            Square.of(file, rank), null);
                }
            }
        }

        public PiecePositioned getPiecePositioned(Square square, Piece piece) {
            PiecePositioned returnValue = null;
            if (piece == null) {
                returnValue = board[square.idx()][12];
            } else {
                returnValue = board[square.idx()][piece.ordinal()];
            }
            return returnValue;
        }

        public PiecePositioned getPosition(Square square) {
            return board[square.idx()][12];
        }


        public static PiecePositionedCache getInstance() {
            return theInstance;
        }
    }

}
