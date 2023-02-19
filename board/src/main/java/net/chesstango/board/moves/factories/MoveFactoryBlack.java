package net.chesstango.board.moves.factories;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.*;
import net.chesstango.board.moves.imp.*;


/**
 * @author Mauricio Coria
 */
public class MoveFactoryBlack extends MoveFactoryAbstract  {

    public static final MoveCastling castlingKingMove = new CastlingBlackKingMove();
    public static final MoveCastling castlingQueenMove = new CastlingBlackQueenMove();

    @Override
    public Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino) {
        return new SimplePawnMove(origen, destino, Cardinal.Sur);
    }

    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare) {
        return new SimpleTwoSquaresPawnMove(origen, destino, enPassantSquare, Cardinal.Sur);
    }

    @Override
    public Move createSimplePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        return new SimplePawnPromotion(origen, destino, Cardinal.Sur, piece);
    }

    @Override
    protected MoveKing addLostCastlingByKingMoveWrapper(MoveKing kingMove) {
        MoveKing result = kingMove;
        if (Square.e8.equals(kingMove.getFrom().getSquare())) {
            result = new MoveDecoratorKingState(kingMove, state -> {
                state.setCastlingBlackQueenAllowed(false);
                state.setCastlingBlackKingAllowed(false);
            });
        }
        return result;
    }

    @Override
    protected Move addLostCastlingByRookMoveWrapper(Move rookMove) {
        Move result = rookMove;
        if (Square.a8.equals(rookMove.getFrom().getSquare())) {
            result = new MoveDecoratorState<Move>(rookMove, state -> state.setCastlingBlackQueenAllowed(false));
        } else if (Square.h8.equals(rookMove.getFrom().getSquare())) {
            result = new MoveDecoratorState<Move>(rookMove, state -> state.setCastlingBlackKingAllowed(false));
        }
        return result;
    }

    @Override
    protected Move addOpponentLostCastlingByRookCapturedWrapper(Move move) {
        Move result = move;
        if (Square.a1.equals(move.getTo().getSquare())) {
            result = new MoveDecoratorState<Move>(move, state -> state.setCastlingWhiteQueenAllowed(false));
        } else if (Square.h1.equals(move.getTo().getSquare())) {
            result = new MoveDecoratorState<Move>(move, state -> state.setCastlingWhiteKingAllowed(false));
        }
        return result;
    }

    @Override
    protected MoveKing addOpponentLostCastlingRookCapturedByKingWrapper(MoveKing move) {
        MoveKing result = move;
        if (Square.a1.equals(move.getTo().getSquare())) {
            result = new MoveDecoratorKingState(move, state -> state.setCastlingWhiteQueenAllowed(false));
        } else if (Square.h1.equals(move.getTo().getSquare())) {
            result = new MoveDecoratorKingState(move, state -> state.setCastlingWhiteKingAllowed(false));
        }
        return result;
    }

    @Override
    protected MovePromotion addOpponentLostCastlingRookCapturedByPromotion(MovePromotion move) {
        MovePromotion result = move;
        if (Square.a1.equals(move.getTo().getSquare())) {
            result = new MoveDecoratorPromotionState(move, state -> state.setCastlingWhiteQueenAllowed(false));
        } else if (Square.h1.equals(move.getTo().getSquare())) {
            result = new MoveDecoratorPromotionState(move, state -> state.setCastlingWhiteKingAllowed(false));
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
