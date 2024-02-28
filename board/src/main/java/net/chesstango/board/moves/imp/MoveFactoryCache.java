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
    public Move createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createSimpleOneSquarePawnMove(from, to);
        }
        return regularMoves[idx];
    }


    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to, Square enPassantSquare) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createSimpleTwoSquaresPawnMove(from, to, enPassantSquare);
        }
        return regularMoves[idx];
    }

    @Override
    public Move createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createCapturePawnMove(from, to, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public Move createCaptureEnPassantPawnMove(PiecePositioned from, PiecePositioned to, PiecePositioned enPassantPawn, Cardinal cardinal) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createCaptureEnPassantPawnMove(from, to, enPassantPawn, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public Move createSimpleKnightMove(PiecePositioned origen, PiecePositioned destino) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createSimpleKnightMove(origen, destino);
        }
        return regularMoves[idx];
    }

    /*
    @Override
    public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createSimpleMove(origen, destino, cardinal);
        }
        return regularMoves[idx];
    }
     */

    @Override
    public Move createCaptureKnightMove(PiecePositioned origen, PiecePositioned destino) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createCaptureKnightMove(origen, destino);
        }
        return regularMoves[idx];
    }

    /*
    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        int idx = Math.abs(computeKey(origen, destino) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || move!=null && !(Objects.equals(origen, move.getFrom()) && Objects.equals(destino, move.getTo())) ){
            regularMoves[idx] = moveFactoryImp.createCaptureMove(origen, destino, cardinal);
        }
        return regularMoves[idx];
    }
     */

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
    public MovePromotion createSimplePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece) {
        return moveFactoryImp.createSimplePromotionPawnMove(from, to, piece);
    }

    @Override
    public MovePromotion createCapturePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece, Cardinal cardinal) {
        return moveFactoryImp.createCapturePromotionPawnMove(from, to, piece, cardinal);
    }

    @Override
    public Move createSimpleBishopnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return moveFactoryImp.createSimpleBishopnMove(origen, destino, cardinal);
    }

    @Override
    public Move createCaptureBishopMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return moveFactoryImp.createCaptureBishopMove(origen, destino, cardinal);
    }

    @Override
    public Move createSimpleQueenMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return moveFactoryImp.createSimpleQueenMove(origen, destino, cardinal);
    }

    @Override
    public Move createCaptureQueenMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return moveFactoryImp.createCaptureQueenMove(origen, destino, cardinal);
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
