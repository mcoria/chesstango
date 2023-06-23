package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class MoveFactoryCache implements MoveFactory {


    private final MoveFactory moveFactoryImp;

    private final Map<Long, Move> regularMoves = new HashMap<>();

    private final Map<Long, MoveKing> movesKings = new HashMap<>();

    public MoveFactoryCache(MoveFactory moveFactoryImp) {
        this.moveFactoryImp = moveFactoryImp;
    }

    @Override
    public Move createSimpleOneSquarePawnMove(PiecePositioned origen, PiecePositioned destino) {
        return regularMoves.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createSimpleOneSquarePawnMove(origen, destino));
    }


    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare) {
        return regularMoves.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createSimpleTwoSquaresPawnMove(origen, destino, enPassantSquare));
    }

    @Override
    public Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return regularMoves.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createCapturePawnMove(origen, destino, cardinal));
    }

    @Override
    public Move createCaptureEnPassantPawnMove(PiecePositioned origen, PiecePositioned destino, PiecePositioned enPassantPawn, Cardinal cardinal) {
        return regularMoves.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createCaptureEnPassantPawnMove(origen, destino, enPassantPawn, cardinal));
    }

    @Override
    public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
        return regularMoves.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createSimpleMove(origen, destino));
    }

    @Override
    public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return regularMoves.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createSimpleMove(origen, destino));
    }

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
        return regularMoves.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createCaptureMove(origen, destino));
    }

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return regularMoves.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createCaptureMove(origen, destino, cardinal));
    }

    @Override
    public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return regularMoves.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createSimpleRookMove(origen, destino, cardinal));
    }

    @Override
    public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return regularMoves.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createCaptureRookMove(origen, destino, cardinal));
    }

    @Override
    public MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino) {
        return movesKings.computeIfAbsent(computeKingKey(origen, destino), key -> moveFactoryImp.createSimpleKingMove(origen, destino));
    }

    @Override
    public MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino) {
        return movesKings.computeIfAbsent(computeKingKey(origen, destino), key -> moveFactoryImp.createSimpleKingMove(origen, destino));
    }

    @Override
    public MoveCastling createCastlingQueenMove() {
        return moveFactoryImp.createCastlingQueenMove();
    }

    @Override
    public MoveCastling createCastlingKingMove() {
        return moveFactoryImp.createCastlingQueenMove();
    }

    @Override
    public MovePromotion createSimplePromotionPawnMove(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        return moveFactoryImp.createSimplePromotionPawnMove(origen, destino, piece);
    }

    @Override
    public MovePromotion createCapturePromotionPawnMove(PiecePositioned origen, PiecePositioned destino, Piece piece, Cardinal cardinal) {
        return moveFactoryImp.createCapturePromotionPawnMove(origen, destino, piece, cardinal);
    }

    private Long computeKey(PiecePositioned origen, PiecePositioned destino) {
        return 0L;
    }

    private Long computeKingKey(PiecePositioned origen, PiecePositioned destino) {
        return 0L;
    }
}
