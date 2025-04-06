package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.Position;
import net.chesstango.board.position.State;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.internal.position.BitBoardImp;
import net.chesstango.board.internal.position.PositionImp;
import net.chesstango.board.internal.position.StateImp;
import net.chesstango.board.internal.position.SquareBoardImp;


/**
 * @author Mauricio Coria
 */
public class ChessPositionBuilder implements PositionBuilder<Position> {
    private final PositionImp chessPosition;
    private final SquareBoard squareBoard;
    private final State state;
    private final BitBoard bitBoard;


    public ChessPositionBuilder() {
        this(new PositionImp(), new SquareBoardImp(), new StateImp(), new BitBoardImp());
        chessPosition.setSquareBoard(squareBoard);
        chessPosition.setState(state);
        chessPosition.setBitBoard(bitBoard);
    }

    ChessPositionBuilder(PositionImp chessPosition, SquareBoard squareBoard, State state, BitBoard bitBoard) {
        this.chessPosition = chessPosition;
        this.squareBoard = squareBoard;
        this.state = state;
        this.bitBoard = bitBoard;
    }

    @Override
    public Position getChessRepresentation() {
        return chessPosition;
    }

    @Override
    public PositionBuilder<Position> withTurn(Color turn) {
        state.setCurrentTurn(turn);
        return this;
    }


    @Override
    public PositionBuilder<Position> withEnPassantSquare(Square enPassantSquare) {
        state.setEnPassantSquare(enPassantSquare);
        return this;
    }


    @Override
    public PositionBuilder<Position> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        state.setCastlingWhiteQueenAllowed(castlingWhiteQueenAllowed);
        return this;
    }

    @Override
    public PositionBuilder<Position> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        state.setCastlingWhiteKingAllowed(castlingWhiteKingAllowed);
        return this;
    }


    @Override
    public PositionBuilder<Position> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        state.setCastlingBlackQueenAllowed(castlingBlackQueenAllowed);
        return this;
    }

    @Override
    public PositionBuilder<Position> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        state.setCastlingBlackKingAllowed(castlingBlackKingAllowed);
        return this;
    }

    @Override
    public PositionBuilder<Position> withHalfMoveClock(int halfMoveClock) {
        state.setHalfMoveClock(halfMoveClock);
        return this;
    }

    @Override
    public PositionBuilder<Position> withFullMoveClock(int fullMoveClock) {
        state.setFullMoveClock(fullMoveClock);
        return this;
    }

    public PositionBuilder<Position> withPiece(Square square, Piece piece) {
        if (piece == null) {
            throw new RuntimeException("piece is null");
        }
        squareBoard.setPiece(square, piece);
        bitBoard.addPosition(PiecePositioned.of(square, piece));
        return this;
    }
}
