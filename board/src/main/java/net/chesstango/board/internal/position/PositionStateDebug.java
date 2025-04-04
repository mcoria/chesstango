package net.chesstango.board.internal.position;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.position.SquareBoard;


/**
 * @author Mauricio Coria
 */
public class PositionStateDebug extends PositionStateImp {

    public void validar() {
        if (this.getCurrentTurn() == null) {
            throw new RuntimeException("Y el turno?");
        }

        if (this.getEnPassantSquare() != null) {
            if (Color.WHITE.equals(this.getCurrentTurn()) && this.getEnPassantSquare().getRank() != 5) {
                throw new RuntimeException("EnPassantSquare mal setteado: " + this.getEnPassantSquare());
            }
            if (Color.BLACK.equals(this.getCurrentTurn()) && this.getEnPassantSquare().getRank() != 2) {
                throw new RuntimeException("EnPassantSquare mal setteado: " + this.getEnPassantSquare());
            }
        }
    }

    public void validar(SquareBoard squareBoard) {
        validar();

        if (this.isCastlingWhiteQueenAllowed()) {
            if (squareBoard.getPiece(Square.a1) == null) {
                throw new RuntimeException(
                        "isCastlingWhiteQueenAllowed mal setteado");
            } else if (!Piece.ROOK_WHITE.equals(squareBoard.getPiece(Square.a1))) {
                throw new RuntimeException(
                        "isCastlingWhiteQueenAllowed mal setteado");
            }
        }

        if (this.isCastlingWhiteKingAllowed()) {
            if (squareBoard.getPiece(Square.h1) == null) {
                throw new RuntimeException(
                        "isCastlingWhiteQueenAllowed mal setteado");
            } else if (!Piece.ROOK_WHITE.equals(squareBoard.getPiece(Square.h1))) {
                throw new RuntimeException(
                        "isCastlingWhiteQueenAllowed mal setteado");
            }
        }

        if (this.isCastlingBlackQueenAllowed()) {
            if (squareBoard.getPiece(Square.a8) == null) {
                throw new RuntimeException(
                        "isCastlingBlackQueenAllowed mal setteado");
            } else if (!Piece.ROOK_BLACK.equals(squareBoard.getPiece(Square.a8))) {
                throw new RuntimeException(
                        "isCastlingBlackQueenAllowed mal setteado");
            }
        }

        if (this.isCastlingBlackKingAllowed()) {
            if (squareBoard.getPiece(Square.h8) == null) {
                throw new RuntimeException(
                        "isCastlingBlackKingAllowed mal setteado");
            } else if (!Piece.ROOK_BLACK.equals(squareBoard.getPiece(Square.h8))) {
                throw new RuntimeException(
                        "isCastlingBlackKingAllowed mal setteado");
            }
        }
    }
}
