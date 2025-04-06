package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.internal.position.PositionStateImp;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.Position;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.internal.position.BitBoardImp;
import net.chesstango.board.internal.position.PositionImp;
import net.chesstango.board.internal.position.SquareBoardImp;


/**
 * @author Mauricio Coria
 */
public class ChessPositionBuilder implements PositionBuilder<Position> {
    private final PositionImp chessPosition;
    private final SquareBoard squareBoard;
    private final PositionState positionState;
    private final BitBoard bitBoard;


    public ChessPositionBuilder() {
        this(new PositionImp(), new SquareBoardImp(), new PositionStateImp(), new BitBoardImp());
        chessPosition.setSquareBoard(squareBoard);
        chessPosition.setPositionState(positionState);
        chessPosition.setBitBoard(bitBoard);
    }

    ChessPositionBuilder(PositionImp chessPosition, SquareBoard squareBoard, PositionState positionState, BitBoard bitBoard) {
        this.chessPosition = chessPosition;
        this.squareBoard = squareBoard;
        this.positionState = positionState;
        this.bitBoard = bitBoard;
    }

    @Override
    public Position getChessRepresentation() {
        return chessPosition;
    }

    @Override
    public PositionBuilder<Position> withTurn(Color turn) {
        positionState.setCurrentTurn(turn);
        return this;
    }


    @Override
    public PositionBuilder<Position> withEnPassantSquare(Square enPassantSquare) {
        positionState.setEnPassantSquare(enPassantSquare);
        return this;
    }


    @Override
    public PositionBuilder<Position> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        positionState.setCastlingWhiteQueenAllowed(castlingWhiteQueenAllowed);
        return this;
    }

    @Override
    public PositionBuilder<Position> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        positionState.setCastlingWhiteKingAllowed(castlingWhiteKingAllowed);
        return this;
    }


    @Override
    public PositionBuilder<Position> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        positionState.setCastlingBlackQueenAllowed(castlingBlackQueenAllowed);
        return this;
    }

    @Override
    public PositionBuilder<Position> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        positionState.setCastlingBlackKingAllowed(castlingBlackKingAllowed);
        return this;
    }

    @Override
    public PositionBuilder<Position> withHalfMoveClock(int halfMoveClock) {
        positionState.setHalfMoveClock(halfMoveClock);
        return this;
    }

    @Override
    public PositionBuilder<Position> withFullMoveClock(int fullMoveClock) {
        positionState.setFullMoveClock(fullMoveClock);
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
