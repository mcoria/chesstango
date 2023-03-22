package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 *
 */
public abstract class MoveFactoryAbstract  implements MoveFactory {
    protected AlgoPiecePositioned algoPiecePositioned = new AlgoPiecePositioned();

    protected AlgoPositionState algoPositionState = new AlgoPositionState();

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
        MovePromotionImp moveImp = new MovePromotionImp(origen, destino, piece);
        moveImp.setFnDoColorBoard(algoColorBoard::captureFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::captureFnUndoColorBoard);
        return moveImp;
    }


    @Override
    public MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino) {
        MoveKingImp moveImp = new MoveKingImp(origen, destino);
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    @Override
    public MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino) {
        MoveKingImp moveImp = new MoveKingImp(origen, destino);
        addCaptureMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }


    /**
     *
     * WIRING
     */


    protected void addSimpleMoveExecutors(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp) {
        if(origen.getPiece().isPawn()) {
            moveImp.setFnUpdatePositionStateBeforeRollTurn(PositionState::resetHalfMoveClock);
        } else if(origen.getPiece().isKing()) {
            moveImp.setFnUpdatePositionStateBeforeRollTurn(this::fnKingUpdatePositionStateBeforeRollTurn);
        } else {
            moveImp.setFnUpdatePositionStateBeforeRollTurn(PositionState::incrementHalfMoveClock);
        }

        moveImp.setFnDoMovePiecePlacement(algoPiecePositioned::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(algoPiecePositioned::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoColorBoard::defaultFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::defaultFnUndoColorBoard);

        if(origen.getPiece().isKing()) {
            moveImp.setFnDoZobrit(this::fnDoZobritKing);
        } else {
            moveImp.setFnDoZobrit(alogZobrit::defaultFnDoZobrit);
        }
    }

    protected void addCaptureMoveExecutors(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp) {
        if(origen.getPiece().isKing()) {
            moveImp.setFnUpdatePositionStateBeforeRollTurn(this::fnKingCaptureUpdatePositionStateBeforeRollTurn);
        } else {
            moveImp.setFnUpdatePositionStateBeforeRollTurn(PositionState::resetHalfMoveClock);
        }

        moveImp.setFnDoMovePiecePlacement(algoPiecePositioned::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(algoPiecePositioned::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoColorBoard::captureFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::captureFnUndoColorBoard);

        moveImp.setFnDoZobrit(alogZobrit::captureFnDoZobrit);
    }

    protected void addSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp, Square enPassantSquare) {
        moveImp.setFnUpdatePositionStateBeforeRollTurn(positionState -> algoPositionState.twoSquaresPawnMove(positionState, enPassantSquare));

        moveImp.setFnDoMovePiecePlacement(algoPiecePositioned::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(algoPiecePositioned::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoColorBoard::defaultFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::defaultFnUndoColorBoard);

        moveImp.setFnDoZobrit(alogZobrit::defaultFnDoZobrit);
    }

    protected abstract void fnKingUpdatePositionStateBeforeRollTurn(PositionState positionState);

    protected abstract void fnKingCaptureUpdatePositionStateBeforeRollTurn(PositionState positionState);

    protected abstract void fnDoZobritKing(PiecePositioned from, PiecePositioned to, ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState);

}
