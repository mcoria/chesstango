package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
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
    public GameBuilder withEnPassantSquare(Square enPassantSquare) {
        chessPositionBuilder.withEnPassantSquare(enPassantSquare);
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

    public GameBuilder withPiece(Square square, Piece piece) {
        chessPositionBuilder.withPiece(square, piece);
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

}
