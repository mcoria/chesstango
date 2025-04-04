package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.factory.ChessInjector;


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
    public Game getChessRepresentation() {
        if (game == null) {
            game = chessInjector.getGame();
        }
        return game;
    }

    @Override
    public PositionBuilder<Game> withTurn(Color turn) {
        chessPositionBuilder.withTurn(turn);
        return this;
    }


    @Override
    public PositionBuilder<Game> withEnPassantSquare(Square enPassantSquare) {
        chessPositionBuilder.withEnPassantSquare(enPassantSquare);
        return this;
    }


    @Override
    public PositionBuilder<Game> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        chessPositionBuilder.withCastlingWhiteQueenAllowed(castlingWhiteQueenAllowed);
        return this;
    }

    @Override
    public PositionBuilder<Game> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        chessPositionBuilder.withCastlingWhiteKingAllowed(castlingWhiteKingAllowed);
        return this;
    }


    @Override
    public PositionBuilder<Game> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        chessPositionBuilder.withCastlingBlackQueenAllowed(castlingBlackQueenAllowed);
        return this;
    }


    @Override
    public PositionBuilder<Game> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        chessPositionBuilder.withCastlingBlackKingAllowed(castlingBlackKingAllowed);
        return this;
    }

    public PositionBuilder<Game> withPiece(Square square, Piece piece) {
        chessPositionBuilder.withPiece(square, piece);
        return this;
    }

    @Override
    public PositionBuilder<Game> withHalfMoveClock(int halfMoveClock) {
        chessPositionBuilder.withHalfMoveClock(halfMoveClock);
        return this;
    }

    @Override
    public PositionBuilder<Game> withFullMoveClock(int fullMoveClock) {
        chessPositionBuilder.withFullMoveClock(fullMoveClock);
        return this;
    }

}
