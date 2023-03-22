package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.moves.MovePromotion;

/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryBlack extends MoveFactoryAbstract{

    @Override
    public Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino) {
        MoveImp moveImp = new MoveImp(origen, destino, Cardinal.Sur);
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare) {
        MoveImp moveImp = new MoveImp(origen, destino, Cardinal.Sur);
        addSimpleTwoSquaresPawnMove(origen, destino, moveImp, enPassantSquare);
        return moveImp;
    }

    @Override
    public MovePromotion createSimplePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        MovePromotionImp moveImp = new MovePromotionImp(origen, destino, Cardinal.Sur, piece);
        return moveImp;
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
