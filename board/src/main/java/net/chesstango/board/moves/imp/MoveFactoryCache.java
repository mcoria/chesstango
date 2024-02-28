package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.*;
import net.chesstango.board.moves.factories.MoveFactory;

import java.util.Objects;

/**
 * EL problema es que no funciona bien en ambientes multithreadsl.
 * Ademas no observo mejoras significativas
 * @author Mauricio Coria
 */
public class MoveFactoryCache implements MoveFactory {

    private final MoveFactory moveFactoryImp;

    private final static int CACHE_SIZE = 1 << 16;

    private final Move[] regularMoves = new Move[CACHE_SIZE];

    //private final Map<Integer, MoveKing> movesKings = new HashMap<>();

    public MoveFactoryCache(MoveFactory moveFactoryImp) {
        this.moveFactoryImp = moveFactoryImp;
    }

    @Override
    public Move createSimpleOneSquarePawnMove(PiecePositioned origen, PiecePositioned destino) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createSimpleOneSquarePawnMove(origen, destino);
        }
        return regularMoves[idx];
    }


    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createSimpleTwoSquaresPawnMove(origen, destino, enPassantSquare);
        }
        return regularMoves[idx];
    }

    @Override
    public Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createCapturePawnMove(origen, destino, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public Move createCaptureEnPassantPawnMove(PiecePositioned origen, PiecePositioned destino, PiecePositioned enPassantPawn, Cardinal cardinal) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createCaptureEnPassantPawnMove(origen, destino, enPassantPawn, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createSimpleMove(origen, destino);
        }
        return regularMoves[idx];
    }

    @Override
    public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createSimpleMove(origen, destino, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createCaptureMove(origen, destino);
        }
        return regularMoves[idx];
    }

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createCaptureMove(origen, destino, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createSimpleRookMove(origen, destino, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createCaptureRookMove(origen, destino, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino) {
        //return movesKings.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createSimpleKingMove(origen, destino));
        return moveFactoryImp.createSimpleKingMove(origen, destino);
    }

    @Override
    public MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino) {
        //return movesKings.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createCaptureKingMove(origen, destino));
        return moveFactoryImp.createCaptureKingMove(origen, destino);
    }

    @Override
    public MoveCastling createCastlingQueenMove() {
        return moveFactoryImp.createCastlingQueenMove();
    }

    @Override
    public MoveCastling createCastlingKingMove() {
        return moveFactoryImp.createCastlingKingMove();
    }

    @Override
    public MovePromotion createSimplePromotionPawnMove(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        return moveFactoryImp.createSimplePromotionPawnMove(origen, destino, piece);
    }

    @Override
    public MovePromotion createCapturePromotionPawnMove(PiecePositioned origen, PiecePositioned destino, Piece piece, Cardinal cardinal) {
        return moveFactoryImp.createCapturePromotionPawnMove(origen, destino, piece, cardinal);
    }

    /*
    protected int computeKey(PiecePositioned origen, PiecePositioned destino) {
        short squaresKey = (short) (origen.getSquare().getBinaryEncodedFrom() | destino.getSquare().getBinaryEncodedTo());
        short pieceKey = (short) (origen.getPiece().getBinaryEncodedFrom() | (destino.getPiece() == null ? 0 : destino.getPiece().getBinaryEncodedTo()));
        return squaresKey ^ pieceKey;
    }*/

    protected int computeKey(PiecePositioned origen, PiecePositioned destino) {
        return origen.hashCode() ^ destino.hashCode();
    }
}
