package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.moves.MovePromotion;

/**
 * @author Mauricio Coria
 *
 */
public abstract class MoveFactoryAbstract  implements MoveFactory {
    protected final AlgoPiecePositioned algoPiecePositioned = new AlgoPiecePositioned();

    protected final AlgoColorBoard algoColorBoard = new AlgoColorBoard();

    protected final AlogZobrit alogZobrit = new AlogZobrit();

    protected final AlgoPositionState algoPositionState;

    protected MoveFactoryAbstract(AlgoPositionState algoPositionState) {
        this.algoPositionState = algoPositionState;
    }

    /*******************************************************************************
     * SIMPLE MOVES
     *
     */
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
    public MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino) {
        MoveKingImp moveImp = new MoveKingImp(origen, destino);
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    @Override
    public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return null;
    }

    /*******************************************************************************
     * CAPTURE MOVES
     *
     */

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
    public MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino) {
        MoveKingImp moveImp = new MoveKingImp(origen, destino);
        addCaptureMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }


    @Override
    public Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return null;
    }

    @Override
    public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return null;
    }

    @Override
    public Move createCaptureEnPassant(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal, PiecePositioned capture) {
        MoveCaptureEnPassant moveImp = new MoveCaptureEnPassant(origen, destino, cardinal, capture);
        return moveImp;
    }

    @Override
    public MovePromotion createCapturePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        MovePromotionImp moveImp = new MovePromotionImp(origen, destino, piece);
        moveImp.setFnDoColorBoard(algoColorBoard::captureFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::captureFnUndoColorBoard);
        return moveImp;
    }

    /*******************************************************************************
     *
     * WIRING
     *
     *
     */


    protected void addSimpleMoveExecutors(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp) {
        if(origen.getPiece().isPawn()) {
            moveImp.setFnDoPositionState(algoPositionState::doSimplePawnMove);
        } else if(origen.getPiece().isKing()) {
            moveImp.setFnDoPositionState(algoPositionState::doSimpleKingPositionState);
        } else {
            moveImp.setFnDoPositionState(algoPositionState::doSimpleNotPawnNorKingMove);
        }

        moveImp.setFnDoMovePiecePlacement(algoPiecePositioned::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(algoPiecePositioned::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoColorBoard::defaultFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::defaultFnUndoColorBoard);

        moveImp.setFnDoZobrit(alogZobrit::defaultFnDoZobrit);
    }

    protected void addCaptureMoveExecutors(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp) {
        if(origen.getPiece().isKing()) {
            moveImp.setFnDoPositionState(algoPositionState::doCaptureKingPositionState);
        } else {
            moveImp.setFnDoPositionState(algoPositionState::doCaptureNotKingPositionState);
        }

        moveImp.setFnDoMovePiecePlacement(algoPiecePositioned::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(algoPiecePositioned::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoColorBoard::captureFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::captureFnUndoColorBoard);

        moveImp.setFnDoZobrit(alogZobrit::defaultFnDoZobrit);
    }

}
