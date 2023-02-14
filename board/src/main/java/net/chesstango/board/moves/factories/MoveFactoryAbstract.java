package net.chesstango.board.moves.factories;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.moves.imp.*;

/**
 * @author Mauricio Coria
 */
public abstract class MoveFactoryAbstract implements MoveFactory {
    @Override
    public MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino) {
        return addLostCastlingByKingMoveWrapper(new SimpleKingMove(origen, destino));
    }

    @Override
    public MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino) {
        return addOpponentLostCastlingRookCapturedByKingWrapper(addLostCastlingByKingMoveWrapper(new CaptureKingMove(origen, destino)));
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
    public Move createCapturePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
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
    public Move createCaptureEnPassant(PiecePositioned origen, PiecePositioned destino,
                                       Cardinal cardinal, PiecePositioned capture) {
        return new CapturePawnEnPassant(origen, destino, capture);
    }

    protected abstract MoveKing addLostCastlingByKingMoveWrapper(MoveKing simpleKingMove);

    protected abstract Move addLostCastlingByRookMoveWrapper(Move rookMove);

    protected abstract Move addOpponentLostCastlingByRookCapturedWrapper(Move move);

    protected abstract MoveKing addOpponentLostCastlingRookCapturedByKingWrapper(MoveKing move);

    protected abstract MovePromotion addOpponentLostCastlingRookCapturedByPromotion(MovePromotion move);
}
