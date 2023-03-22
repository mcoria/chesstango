package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.position.PiecePlacementWriter;

/**
 * @author Mauricio Coria
 *
 */
public abstract class MoveFactoryAbstract  implements MoveFactory {

    public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
        MoveImp moveImp = new MoveImp(origen, destino);
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }


    public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        MoveImp moveImp = new MoveImp(origen, destino, cardinal);
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    protected void addSimpleMoveExecutors(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp) {
        if(origen.getPiece().isPawn()) {
            moveImp.setFnUpdatePositionStateBeforeRollTurn(positionState -> {
                positionState.resetHalfMoveClock();
            });
        } else {
            moveImp.setFnUpdatePositionStateBeforeRollTurn(positionState -> {
                positionState.incrementHalfMoveClock();
            });
        }

        moveImp.setFnDoMovePiecePlacement(MoveFactoryAbstract::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(MoveFactoryAbstract::defaultFnUndoMovePiecePlacement);
    }

    protected void addSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp, Square enPassantSquare) {
        moveImp.setFnUpdatePositionStateBeforeRollTurn(positionState -> {
            positionState.resetHalfMoveClock();
            positionState.setEnPassantSquare(enPassantSquare);
        });

        moveImp.setFnDoMovePiecePlacement(MoveFactoryAbstract::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(MoveFactoryAbstract::defaultFnUndoMovePiecePlacement);
    }

    protected void addPawnPromotion(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp, Piece piece) {
        moveImp.setFnUpdatePositionStateBeforeRollTurn(positionState -> {
            positionState.resetHalfMoveClock();
        });

        moveImp.setFnDoMovePiecePlacement(MoveFactoryAbstract::fnDoMovePromotion);
        moveImp.setFnUndoMovePiecePlacement(MoveFactoryAbstract::defaultFnUndoMovePiecePlacement);
    }

    private static void fnDoMovePromotion(PiecePositioned piecePositioned, PiecePositioned piecePositioned1, PiecePlacementWriter piecePlacementWriter) {
    }

    private static void defaultFnDoMovePiecePlacement(PiecePositioned from, PiecePositioned to, PiecePlacementWriter piecePlacementWriter) {
        piecePlacementWriter.move(from, to);
    }

    private static void defaultFnUndoMovePiecePlacement(PiecePositioned from, PiecePositioned to, PiecePlacementWriter piecePlacementWriter) {
        piecePlacementWriter.setPosicion(from);
        piecePlacementWriter.setPosicion(to);
    }

}
