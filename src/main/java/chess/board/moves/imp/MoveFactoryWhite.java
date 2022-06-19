package chess.board.moves.imp;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.moves.Move;
import chess.board.moves.MoveCastling;
import chess.board.moves.MoveFactory;
import chess.board.moves.MoveKing;


/**
 * @author Mauricio Coria
 */
public class MoveFactoryWhite implements MoveFactory {

    public static final MoveCastling castlingKingMove = new CastlingWhiteKingMove();
    public static final MoveCastling castlingQueenMove = new CastlingWhiteQueenMove();

    @Override
    public MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino) {
        return addLostCastlingByKingMoveWrapper(new SimpleKingMove(origen, destino));
    }

    @Override
    public MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino) {
        return addLostCastlingByKingMoveWrapper(createCaptureKingMoveImp(origen, destino));
    }

    @Override
    public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino) {
        return addLostCastlingByRookMoveWrapper(createSimpleMove(origen, destino));
    }

    @Override
    public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino) {
        return addLostCastlingByRookMoveWrapper(createCaptureMove(origen, destino));
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
    public Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino) {
        return new SimplePawnMove(origen, destino, Cardinal.Norte);
    }

    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare) {
        return new SimpleTwoSquaresPawnMove(origen, destino, enPassantSquare, Cardinal.Norte);
    }

    @Override
    public Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return addOpponentLostCastlingByRookCaptureWrapper(new CapturePawnMove(origen, destino, cardinal));
    }

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
        return addOpponentLostCastlingByRookCaptureWrapper(new CaptureMove(origen, destino));
    }

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return addOpponentLostCastlingByRookCaptureWrapper(new CaptureMove(origen, destino, cardinal));
    }


    @Override
    public Move createCaptureEnPassant(PiecePositioned origen, PiecePositioned destino,
                                       PiecePositioned capturaEnPassant) {
        return new CapturePawnEnPassant(origen, destino, capturaEnPassant);
    }


    @Override
    public Move createSimplePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        return new SimplePawnPromotion(origen, destino, piece);
    }


    @Override
    public Move createCapturePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        return addOpponentLostCastlingByRookCaptureWrapper(new CapturePawnPromotion(origen, destino, piece));
    }

    protected MoveKing addLostCastlingByKingMoveWrapper(MoveKing kingMove) {
        MoveKing result = kingMove;
        if (Square.e1.equals(kingMove.getFrom().getKey())) {
            result =  new MoveDecoratorKingState(kingMove, state -> {
                state.setCastlingWhiteQueenAllowed(false);
                state.setCastlingWhiteKingAllowed(false);
            });
        }
        return result;
    }

    protected Move addOpponentLostCastlingByRookCaptureWrapper(Move move) {
        Move result = move;
        if (Square.a8.equals(move.getTo().getKey())) {
            result = new MoveDecoratorState(move, state -> state.setCastlingBlackQueenAllowed(false));
        } else if (Square.h8.equals(move.getTo().getKey())) {
            result = new MoveDecoratorState(move, state -> state.setCastlingBlackKingAllowed(false));
        }
        return result;
    }

    protected MoveKing createCaptureKingMoveImp(PiecePositioned origen, PiecePositioned destino) {
        MoveKing kingMove = new CaptureKingMove(origen, destino);
        if (Square.a8.equals(destino.getKey())) {
            kingMove = new MoveDecoratorKingState(kingMove, state -> state.setCastlingBlackQueenAllowed(false));
        } else if (Square.h8.equals(destino.getKey())) {
            kingMove = new MoveDecoratorKingState(kingMove, state -> state.setCastlingBlackKingAllowed(false));
        }
        return kingMove;
    }

    protected Move addLostCastlingByRookMoveWrapper(Move rookMove) {
        Move result = rookMove;
        if (Square.a1.equals(rookMove.getFrom().getKey())) {
            result = new MoveDecoratorState(rookMove, state -> state.setCastlingWhiteQueenAllowed(false));
        } else if (Square.h1.equals(rookMove.getFrom().getKey())) {
            result = new MoveDecoratorState(rookMove, state -> state.setCastlingWhiteKingAllowed(false));
        }
        return result;
    }

    @Override
    public MoveCastling createCastlingQueenMove() {
        return castlingQueenMove;
    }

    @Override
    public MoveCastling createCastlingKingMove() {
        return castlingKingMove;
    }
}
