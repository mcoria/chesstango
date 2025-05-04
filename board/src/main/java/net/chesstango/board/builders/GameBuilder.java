package net.chesstango.board.builders;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.internal.factory.ChessInjector;
import net.chesstango.board.representations.PositionBuilder;


/**
 * @author Mauricio Coria
 */
public class GameBuilder implements PositionBuilder<Game> {
    private final ChessPositionBuilder chessPositionBuilder;
    private final ChessInjector chessInjector;

    private Game game = null;

    public GameBuilder() {
        this(new ChessInjector());
    }

    GameBuilder(ChessInjector chessInjector) {
        this.chessInjector = chessInjector;
        this.chessPositionBuilder = new ChessPositionBuilder(
                chessInjector.getChessPosition(),
                chessInjector.getSquareBoard(),
                chessInjector.getPositionState(),
                chessInjector.getBitBoard()
        );
    }

    @Override
    public Game getPositionRepresentation() {
        if (game == null) {
            game = chessInjector.getGame();
        }
        return game;
    }

    @Override
    public GameBuilder withWhiteTurn(boolean whiteTurn) {
        chessPositionBuilder.withWhiteTurn(whiteTurn);
        return this;
    }

    @Override
    public GameBuilder withWhiteKing(int file, int rank) {
        return withPiece(file, rank, Piece.KING_WHITE);
    }

    @Override
    public GameBuilder withWhiteQueen(int file, int rank) {
        return withPiece(file, rank, Piece.QUEEN_WHITE);
    }

    @Override
    public GameBuilder withWhiteRook(int file, int rank) {
        return withPiece(file, rank, Piece.ROOK_WHITE);
    }

    @Override
    public GameBuilder withWhiteBishop(int file, int rank) {
        return withPiece(file, rank, Piece.BISHOP_WHITE);
    }

    @Override
    public GameBuilder withWhiteKnight(int file, int rank) {
        return withPiece(file, rank, Piece.KNIGHT_WHITE);
    }

    @Override
    public GameBuilder withWhitePawn(int file, int rank) {
        return withPiece(file, rank, Piece.PAWN_WHITE);
    }

    @Override
    public GameBuilder withBlackKing(int file, int rank) {
        return withPiece(file, rank, Piece.KING_BLACK);
    }

    @Override
    public GameBuilder withBlackQueen(int file, int rank) {
        return withPiece(file, rank, Piece.QUEEN_BLACK);
    }

    @Override
    public GameBuilder withBlackRook(int file, int rank) {
        return withPiece(file, rank, Piece.ROOK_BLACK);
    }

    @Override
    public GameBuilder withBlackBishop(int file, int rank) {
        return withPiece(file, rank, Piece.BISHOP_BLACK);
    }

    @Override
    public GameBuilder withBlackKnight(int file, int rank) {
        return withPiece(file, rank, Piece.KNIGHT_BLACK);
    }

    @Override
    public GameBuilder withBlackPawn(int file, int rank) {
        return withPiece(file, rank, Piece.PAWN_BLACK);
    }

    @Override
    public GameBuilder withEnPassantSquare(int file, int rank) {
        chessPositionBuilder.withEnPassantSquare(file, rank);
        return this;
    }


    @Override
    public GameBuilder withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        chessPositionBuilder.withCastlingWhiteQueenAllowed(castlingWhiteQueenAllowed);
        return this;
    }

    @Override
    public GameBuilder withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        chessPositionBuilder.withCastlingWhiteKingAllowed(castlingWhiteKingAllowed);
        return this;
    }


    @Override
    public GameBuilder withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        chessPositionBuilder.withCastlingBlackQueenAllowed(castlingBlackQueenAllowed);
        return this;
    }


    @Override
    public GameBuilder withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        chessPositionBuilder.withCastlingBlackKingAllowed(castlingBlackKingAllowed);
        return this;
    }


    @Override
    public GameBuilder withHalfMoveClock(int halfMoveClock) {
        chessPositionBuilder.withHalfMoveClock(halfMoveClock);
        return this;
    }

    @Override
    public GameBuilder withFullMoveClock(int fullMoveClock) {
        chessPositionBuilder.withFullMoveClock(fullMoveClock);
        return this;
    }

    public GameBuilder withPiece(int file, int rank, Piece piece) {
        chessPositionBuilder.withPiece(file, rank, piece);
        return this;
    }

}
