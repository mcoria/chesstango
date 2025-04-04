package net.chesstango.board.moves.factories.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.moves.factories.MoveFactory;

import java.util.Objects;

/**
 * EL problema es que no funciona bien en ambientes multithreadsl.
 * Ademas no observo mejoras significativas
 *
 * @author Mauricio Coria
 */
public class MoveFactoryCache implements MoveFactory {

    private final MoveFactory moveFactoryImp;

    private final static int CACHE_SIZE = 1 << 16;

    private final PseudoMove[] regularMoves = new PseudoMove[CACHE_SIZE];

    //private final Map<Integer, MoveKing> movesKings = new HashMap<>();

    public MoveFactoryCache(MoveFactory moveFactoryImp) {
        this.moveFactoryImp = moveFactoryImp;
    }

    @Override
    public PseudoMove createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if (move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))) {
            regularMoves[idx] = moveFactoryImp.createSimpleOneSquarePawnMove(from, to);
        }
        return regularMoves[idx];
    }


    @Override
    public PseudoMove createSimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to, Square enPassantSquare) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if (move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))) {
            regularMoves[idx] = moveFactoryImp.createSimpleTwoSquaresPawnMove(from, to, enPassantSquare);
        }
        return regularMoves[idx];
    }

    @Override
    public PseudoMove createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if (move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))) {
            regularMoves[idx] = moveFactoryImp.createCapturePawnMove(from, to, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public PseudoMove createCaptureEnPassantPawnMove(PiecePositioned from, PiecePositioned to, PiecePositioned enPassantPawn, Cardinal cardinal) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if (move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))) {
            regularMoves[idx] = moveFactoryImp.createCaptureEnPassantPawnMove(from, to, enPassantPawn, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public PseudoMove createSimpleKnightMove(PiecePositioned from, PiecePositioned to) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if (move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))) {
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
    public PseudoMove createCaptureKnightMove(PiecePositioned from, PiecePositioned to) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if (move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))) {
            regularMoves[idx] = moveFactoryImp.createCaptureKnightMove(from, to);
        }
        return regularMoves[idx];
    }

    @Override
    public PseudoMove createSimpleRookMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        int idx = Math.abs(computeKey(from, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if (move == null || !(Objects.equals(from, move.getFrom()) && Objects.equals(to, move.getTo()))) {
            regularMoves[idx] = moveFactoryImp.createSimpleRookMove(from, to, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public PseudoMove createCaptureRookMove(PiecePositioned form, PiecePositioned to, Cardinal cardinal) {
        int idx = Math.abs(computeKey(form, to) % CACHE_SIZE);
        Move move = regularMoves[idx];
        if (move == null || !(Objects.equals(form, move.getFrom()) && Objects.equals(to, move.getTo()))) {
            regularMoves[idx] = moveFactoryImp.createCaptureRookMove(form, to, cardinal);
        }
        return regularMoves[idx];
    }

    @Override
    public PseudoMove createSimpleKingMove(PiecePositioned from, PiecePositioned to) {
        //return movesKings.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createSimpleKingMove(origen, destino));
        return moveFactoryImp.createSimpleKingMove(from, to);
    }

    @Override
    public PseudoMove createCaptureKingMove(PiecePositioned from, PiecePositioned to) {
        //return movesKings.computeIfAbsent(computeKey(origen, destino), key -> moveFactoryImp.createCaptureKingMove(origen, destino));
        return moveFactoryImp.createCaptureKingMove(from, to);
    }

    @Override
    public PseudoMove createCastlingQueenMove() {
        return moveFactoryImp.createCastlingQueenMove();
    }

    @Override
    public PseudoMove createCastlingKingMove() {
        return moveFactoryImp.createCastlingKingMove();
    }

    @Override
    public PseudoMove createSimplePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece) {
        return moveFactoryImp.createSimplePromotionPawnMove(from, to, piece);
    }

    @Override
    public PseudoMove createCapturePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece, Cardinal cardinal) {
        return moveFactoryImp.createCapturePromotionPawnMove(from, to, piece, cardinal);
    }

    @Override
    public PseudoMove createSimpleBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return moveFactoryImp.createSimpleBishopMove(from, to, cardinal);
    }

    @Override
    public PseudoMove createCaptureBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return moveFactoryImp.createCaptureBishopMove(from, to, cardinal);
    }

    @Override
    public PseudoMove createSimpleQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return moveFactoryImp.createSimpleQueenMove(from, to, cardinal);
    }

    @Override
    public PseudoMove createCaptureQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return moveFactoryImp.createCaptureQueenMove(from, to, cardinal);
    }

    /*
    protected int computeKey(PiecePositioned origen, PiecePositioned destino) {
        short squaresKey = (short) (origen.getSquare().getBinaryEncodedFrom() | destino.getSquare().getBinaryEncodedTo());
        short pieceKey = (short) (origen.getPiece().getBinaryEncodedFrom() | (destino.getPiece() == null ? 0 : destino.getPiece().getBinaryEncodedTo()));
        return squaresKey ^ pieceKey;
    }*/

    public int computeKey(PiecePositioned origen, PiecePositioned destino) {
        return origen.hashCode() ^ destino.hashCode();
    }
}
