package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.moves.MovePromotion;

/**
 * @author Mauricio Coria
 */
public abstract class MoveFactoryAbstract implements MoveFactory {
    @Override
    public MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino) {
        return new SimpleKingMove(origen, destino);
    }

    @Override
    public MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino) {
        return addOpponentLostCastlingRookCapturedByKingWrapper(new CaptureKingMove(origen, destino));
    }

    @Override
    public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return addLostCastlingByRookMoveWrapper(createSimpleMove(origen, destino));
    }

    @Override
    public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return addLostCastlingByRookMoveWrapper(createCaptureMove(origen, destino, cardinal));
    }

    @Override
    public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
        return new SimpleMove(origen, destino);
    }

    @Override
    public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return new SimpleMove(origen, destino, cardinal);
    }

    @Override
    public Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return addOpponentLostCastlingByRookCapturedWrapper(new CapturePawnMove(origen, destino, cardinal));
    }

    @Override
    public MovePromotion createCapturePromotionPawnMove(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        return addOpponentLostCastlingRookCapturedByPromotion(new CapturePawnPromotion(origen, destino, piece));
    }

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
        return addOpponentLostCastlingByRookCapturedWrapper(new CaptureMove(origen, destino));
    }

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return addOpponentLostCastlingByRookCapturedWrapper(new CaptureMove(origen, destino, cardinal));
    }

    @Override
    public Move createCaptureEnPassantPawnMove(PiecePositioned origen, PiecePositioned destino,
                                               PiecePositioned enPassantPawn, Cardinal cardinal) {
        return new CapturePawnEnPassant(origen, destino, enPassantPawn);
    }

    protected abstract Move addLostCastlingByRookMoveWrapper(Move rookMove);

    protected abstract Move addOpponentLostCastlingByRookCapturedWrapper(Move move);

    protected abstract MoveKing addOpponentLostCastlingRookCapturedByKingWrapper(MoveKing move);

    protected abstract MovePromotion addOpponentLostCastlingRookCapturedByPromotion(MovePromotion move);
}
