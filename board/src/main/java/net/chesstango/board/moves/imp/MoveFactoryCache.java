package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
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

    private final MoveImp[] regularMoves = new MoveImp[CACHE_SIZE];

    //private final Map<Integer, MoveKing> movesKings = new HashMap<>();

    public MoveFactoryCache(MoveFactory moveFactoryImp) {
        this.moveFactoryImp = moveFactoryImp;
    }

    @Override
    public MoveImp createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))){
            regularMoves[idx] = moveFactoryImp.createSimpleOneSquarePawnMove(from, to);
        }
        return regularMoves[idx];
    }


    @Override
    public MoveImp createSimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to, Square enPassantSquare) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))){
            regularMoves[idx] = moveFactoryImp.createSimpleTwoSquaresPawnMove(from, to, enPassantSquare);
        }
        return regularMoves[idx];
    }

    @Override
    public MoveImp createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))){
            regularMoves[idx] = moveFactoryImp.createCapturePawnMove(from, to, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public MoveImp createCaptureEnPassantPawnMove(PiecePositioned from, PiecePositioned to, PiecePositioned enPassantPawn, Cardinal cardinal) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))){
            regularMoves[idx] = moveFactoryImp.createCaptureEnPassantPawnMove(from, to, enPassantPawn, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public MoveImp createSimpleKnightMove(PiecePositioned from, PiecePositioned to) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))){
            regularMoves[idx] = moveFactoryImp.createSimpleKnightMove(from, to);
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
    public MoveImp createCaptureKnightMove(PiecePositioned from, PiecePositioned to) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))){
            regularMoves[idx] = moveFactoryImp.createCaptureKnightMove(from, to);
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
    public MoveImp createSimpleRookMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))){
            regularMoves[idx] = moveFactoryImp.createSimpleRookMove(from, to, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public MoveImp createCaptureRookMove(PiecePositioned form, PiecePositioned to, Cardinal cardinal) {
        int idx = Math.abs(computeKey(form, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if(move == null || !(Objects.equals(form, move.getFrom()) && Objects.equals(to, move.getTo()))){
            regularMoves[idx] = moveFactoryImp.createCaptureRookMove(form, to, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public MoveKingImp createSimpleKingMove(PiecePositioned from, PiecePositioned to) {
        //return movesKings.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createSimpleKingMove(origen, destino));
        return moveFactoryImp.createSimpleKingMove(from, to);
    }

    @Override
    public MoveKingImp createCaptureKingMove(PiecePositioned from, PiecePositioned to) {
        //return movesKings.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createCaptureKingMove(origen, destino));
        return moveFactoryImp.createCaptureKingMove(from, to);
    }

    @Override
    public MoveCastlingImp createCastlingQueenMove() {
        return moveFactoryImp.createCastlingQueenMove();
    }

    @Override
    public MoveCastlingImp createCastlingKingMove() {
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
    public MoveImp createSimpleBishopnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return moveFactoryImp.createSimpleBishopnMove(from, to, cardinal);
    }

    @Override
    public MoveImp createCaptureBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return moveFactoryImp.createCaptureBishopMove(from, to, cardinal);
    }

    @Override
    public MoveImp createSimpleQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return moveFactoryImp.createSimpleQueenMove(from, to, cardinal);
    }

    @Override
    public MoveImp createCaptureQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return moveFactoryImp.createCaptureQueenMove(from, to, cardinal);
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
