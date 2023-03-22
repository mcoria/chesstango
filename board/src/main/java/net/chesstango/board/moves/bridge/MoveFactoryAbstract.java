package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.MovePromotion;

/**
 * @author Mauricio Coria
 *
 */
public abstract class MoveFactoryAbstract  implements MoveFactory {
    protected AlgoPiecePositioned algoPiecePositioned = new AlgoPiecePositioned();

    protected AlgoColorBoard algoColorBoard = new AlgoColorBoard();

    protected AlogZobrit alogZobrit = new AlogZobrit();

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

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
        MoveImp moveImp = new MoveImp(origen, destino);
        addCaptureMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    @Override
    public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        MoveImp moveImp = new MoveImp(origen, destino, cardinal);
        addCaptureMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    @Override
    public MovePromotion createCapturePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        MovePromotionImp moveImp = new MovePromotionImp(origen, destino, Cardinal.Norte, piece);
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

        moveImp.setFnDoMovePiecePlacement(algoPiecePositioned::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(algoPiecePositioned::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoColorBoard::defaultFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::defaultFnUndoColorBoard);

        moveImp.setFnDoZobrit(alogZobrit::defaultFnDoZobrit);
    }

    protected void addSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp, Square enPassantSquare) {
        moveImp.setFnUpdatePositionStateBeforeRollTurn(positionState -> {
            positionState.resetHalfMoveClock();
            positionState.setEnPassantSquare(enPassantSquare);
        });

        moveImp.setFnDoMovePiecePlacement(algoPiecePositioned::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(algoPiecePositioned::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoColorBoard::defaultFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::defaultFnUndoColorBoard);

        moveImp.setFnDoZobrit(alogZobrit::defaultFnDoZobrit);
    }

    protected void addCaptureMoveExecutors(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp) {
        if(origen.getPiece().isPawn()) {
            moveImp.setFnUpdatePositionStateBeforeRollTurn(positionState -> {
                positionState.resetHalfMoveClock();
            });
        } else {
            moveImp.setFnUpdatePositionStateBeforeRollTurn(positionState -> {
                positionState.resetHalfMoveClock();
            });
        }

        moveImp.setFnDoMovePiecePlacement(algoPiecePositioned::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(algoPiecePositioned::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoColorBoard::captureFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::captureFnUndoColorBoard);

        moveImp.setFnDoZobrit(alogZobrit::captureFnDoZobrit);
    }


}
