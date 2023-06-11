package net.chesstango.board.moves.impl.bridge;

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
    protected final AlgoSquareBoard algoSquareBoard = new AlgoSquareBoard();

    protected final AlgoBitBoard algoBitBoard = new AlgoBitBoard();

    protected final AlgoZobrist algoZobrist = new AlgoZobrist();

    protected final AlgoPositionState algoPositionState;

    protected abstract Cardinal getPawnDirection();

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
    public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        MoveImp moveImp = new MoveImp(origen, destino);
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
    public Move createSimpleOneSquarePawnMove(PiecePositioned origen, PiecePositioned destino) {
        MoveImp moveImp = new MoveImp(origen, destino, getPawnDirection());
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare) {
        MovePawnTwoSquares moveImp = new MovePawnTwoSquares(origen, destino, getPawnDirection(), enPassantSquare);
        return moveImp;
    }

    @Override
    public MovePromotion createSimplePromotionPawnMove(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        MovePawnPromotion moveImp = new MovePawnPromotion(origen, destino, getPawnDirection(), piece);
        moveImp.setFnDoBitBoard(algoBitBoard::defaultFnDoBitBoard);
        moveImp.setFnUndoBitBoard(algoBitBoard::defaultFnUndoBitBoard);
        return moveImp;
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
    public Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        MoveImp moveImp = new MoveImp(origen, destino, cardinal);
        addCaptureMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    @Override
    public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
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
    public Move createCaptureEnPassantPawnMove(PiecePositioned origen, PiecePositioned destino, PiecePositioned enPassantPawn, Cardinal cardinal) {
        MovePawnCaptureEnPassant moveImp = new MovePawnCaptureEnPassant(origen, destino, cardinal, enPassantPawn);
        return moveImp;
    }

    @Override
    public MovePromotion createCapturePromotionPawnMove(PiecePositioned origen, PiecePositioned destino, Piece piece, Cardinal cardinal) {
        MovePawnPromotion moveImp = new MovePawnPromotion(origen, destino, piece);
        moveImp.setFnDoBitBoard(algoBitBoard::captureFnDoBitBoard);
        moveImp.setFnUndoBitBoard(algoBitBoard::captureFnUndoBitBoard);
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

        moveImp.setFnDoSquareBoard(algoSquareBoard::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoSquareBoard(algoSquareBoard::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoBitBoard::defaultFnDoBitBoard);
        moveImp.setFnUndoColorBoard(algoBitBoard::defaultFnUndoBitBoard);

        moveImp.setFnDoZobrist(algoZobrist::defaultFnDoZobrit);
    }

    protected void addCaptureMoveExecutors(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp) {
        if(origen.getPiece().isKing()) {
            moveImp.setFnDoPositionState(algoPositionState::doCaptureKingPositionState);
        } else {
            moveImp.setFnDoPositionState(algoPositionState::doCaptureNotKingPositionState);
        }

        moveImp.setFnDoSquareBoard(algoSquareBoard::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoSquareBoard(algoSquareBoard::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoBitBoard::captureFnDoBitBoard);
        moveImp.setFnUndoColorBoard(algoBitBoard::captureFnUndoBitBoard);

        moveImp.setFnDoZobrist(algoZobrist::defaultFnDoZobrit);
    }

}
