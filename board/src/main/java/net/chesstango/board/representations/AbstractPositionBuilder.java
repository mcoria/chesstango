package net.chesstango.board.representations;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractPositionBuilder<T> implements PositionBuilder<T> {
    protected boolean whiteTurn;
    protected long enPassantSquare;
    protected boolean castlingBlackKingAllowed;
    protected boolean castlingBlackQueenAllowed;
    protected boolean castlingWhiteKingAllowed;
    protected boolean castlingWhiteQueenAllowed;
    protected int halfMoveClock;
    protected int fullMoveClock;

    protected long whitePositions = 0;
    protected long blackPositions = 0;
    protected long kingPositions = 0;
    protected long queenPositions = 0;
    protected long rookPositions = 0;
    protected long bishopPositions = 0;
    protected long knightPositions = 0;
    protected long pawnPositions = 0;


    @Override
    public AbstractPositionBuilder<T> withWhiteTurn(boolean whiteTurn) {
        this.whiteTurn = whiteTurn;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withWhiteKing(int file, int rank) {
        int square = rank * 8 + file;
        whitePositions |= 1L << square;
        kingPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withWhiteQueen(int file, int rank) {
        int square = rank * 8 + file;
        whitePositions |= 1L << square;
        queenPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withWhiteRook(int file, int rank) {
        int square = rank * 8 + file;
        whitePositions |= 1L << square;
        rookPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withWhiteBishop(int file, int rank) {
        int square = rank * 8 + file;
        whitePositions |= 1L << square;
        bishopPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withWhiteKnight(int file, int rank) {
        int square = rank * 8 + file;
        whitePositions |= 1L << square;
        knightPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withWhitePawn(int file, int rank) {
        int square = rank * 8 + file;
        whitePositions |= 1L << square;
        pawnPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withBlackKing(int file, int rank) {
        int square = rank * 8 + file;
        blackPositions |= 1L << square;
        kingPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withBlackQueen(int file, int rank) {
        int square = rank * 8 + file;
        blackPositions |= 1L << square;
        queenPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withBlackRook(int file, int rank) {
        int square = rank * 8 + file;
        blackPositions |= 1L << square;
        rookPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withBlackBishop(int file, int rank) {
        int square = rank * 8 + file;
        blackPositions |= 1L << square;
        bishopPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withBlackKnight(int file, int rank) {
        int square = rank * 8 + file;
        blackPositions |= 1L << square;
        knightPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withBlackPawn(int file, int rank) {
        int square = rank * 8 + file;
        blackPositions |= 1L << square;
        pawnPositions |= 1L << square;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withEnPassantSquare(int file, int rank) {
        this.enPassantSquare = 1L << (rank * 8 + file);
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        this.castlingBlackKingAllowed = castlingBlackKingAllowed;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        this.castlingBlackQueenAllowed = castlingBlackQueenAllowed;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        this.castlingWhiteKingAllowed = castlingWhiteKingAllowed;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        this.castlingWhiteQueenAllowed = castlingWhiteQueenAllowed;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withFullMoveClock(int fullMoveClock) {
        this.fullMoveClock = fullMoveClock;
        return this;
    }

}
