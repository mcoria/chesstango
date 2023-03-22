package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveKing;

/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryBlack extends MoveFactoryAbstract{
    @Override
    public Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino) {
        return null;
    }

    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare) {
        return null;
    }

    @Override
    public Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return null;
    }

    @Override
    public Move createCaptureEnPassant(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal, PiecePositioned capture) {
        return null;
    }

    @Override
    public Move createSimplePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        return null;
    }

    @Override
    public Move createCapturePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        return null;
    }

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
        return null;
    }

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return null;
    }

    @Override
    public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return null;
    }

    @Override
    public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return null;
    }

    @Override
    public MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino) {
        return null;
    }

    @Override
    public MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino) {
        return null;
    }

    @Override
    public MoveCastling createCastlingQueenMove() {
        return null;
    }

    @Override
    public MoveCastling createCastlingKingMove() {
        return null;
    }
}