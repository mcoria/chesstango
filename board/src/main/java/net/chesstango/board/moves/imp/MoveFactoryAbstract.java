package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.moves.factories.MoveFactory;

/**
 * @author Mauricio Coria
 */
public abstract class MoveFactoryAbstract implements MoveFactory {
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
    @Override
    public Move createSimpleKnightMove(PiecePositioned from, PiecePositioned to) {
        MoveComposed moveImp = new MoveComposed(from, to);
        addSimpleMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public Move createSimpleBishopnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return createSimpleMoveImp(from, to, cardinal);
    }

    @Override
    public Move createSimpleQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return createSimpleMoveImp(from, to, cardinal);
    }

    @Override
    public Move createSimpleRookMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        MoveComposed moveImp = new MoveComposed(from, to);
        addSimpleMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public net.chesstango.board.moves.MoveKing createSimpleKingMove(PiecePositioned from, PiecePositioned to) {
        MoveKingImp moveImp = new MoveKingImp(from, to);
        addSimpleMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public Move createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to) {
        MoveComposed moveImp = new MoveComposed(from, to, getPawnDirection());
        addSimpleMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to, Square enPassantSquare) {
        MovePawnTwoSquares moveImp = new MovePawnTwoSquares(from, to, getPawnDirection(), enPassantSquare);
        return moveImp;
    }

    @Override
    public MovePromotion createSimplePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece) {
        MovePawnPromotion moveImp = new MovePawnPromotion(from, to, getPawnDirection(), piece);
        return moveImp;
    }

    /*******************************************************************************
     * CAPTURE MOVES
     *
     */

    @Override
    public Move createCaptureKnightMove(PiecePositioned from, PiecePositioned to) {
        MoveComposed moveImp = new MoveComposed(from, to);
        addCaptureMoveExecutors(from, to, moveImp);
        return moveImp;
    }
    
    @Override
    public Move createCaptureBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return createCaptureMoveImp(from, to, cardinal);
    }

    @Override
    public Move createCaptureQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return createCaptureMoveImp(from, to, cardinal);
    }

    @Override
    public Move createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        MoveComposed moveImp = new MoveComposed(from, to, cardinal);
        addCaptureMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public Move createCaptureRookMove(PiecePositioned form, PiecePositioned to, Cardinal cardinal) {
        MoveComposed moveImp = new MoveComposed(form, to, cardinal);
        addCaptureMoveExecutors(form, to, moveImp);
        return moveImp;
    }

    @Override
    public net.chesstango.board.moves.MoveKing createCaptureKingMove(PiecePositioned from, PiecePositioned to) {
        MoveKingImp moveImp = new MoveKingImp(from, to);
        addCaptureMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public Move createCaptureEnPassantPawnMove(PiecePositioned from, PiecePositioned to, PiecePositioned enPassantPawn, Cardinal cardinal) {
        MovePawnCaptureEnPassant moveImp = new MovePawnCaptureEnPassant(from, to, cardinal, enPassantPawn);
        return moveImp;
    }

    @Override
    public MovePromotion createCapturePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece, Cardinal cardinal) {
        MovePawnPromotion moveImp = new MovePawnPromotion(from, to, piece);
        return moveImp;
    }

    /*******************************************************************************
     *
     * WIRING
     *
     *
     */

    public Move createSimpleMoveImp(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        MoveComposed moveImp = new MoveComposed(origen, destino, cardinal);
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    protected Move createCaptureMoveImp(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        MoveComposed moveImp = new MoveComposed(origen, destino, cardinal);
        addCaptureMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }


    protected void addSimpleMoveExecutors(PiecePositioned origen, PiecePositioned destino, MoveComposed moveImp) {
        if (origen.getPiece().isPawn()) {
            moveImp.setFnDoPositionState(algoPositionState::doSimplePawnMove);
        } else if (origen.getPiece().isKing()) {
            moveImp.setFnDoPositionState(algoPositionState::doSimpleKingPositionState);
        } else {
            moveImp.setFnDoPositionState(algoPositionState::doSimpleNotPawnNorKingMove);
        }

        moveImp.setFnDoSquareBoard(algoSquareBoard::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoSquareBoard(algoSquareBoard::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoBitBoard::defaultFnDoBitBoard);
        moveImp.setFnUndoColorBoard(algoBitBoard::defaultFnUndoBitBoard);

        moveImp.setFnDoZobrist(algoZobrist::defaultFnDoZobrist);
    }

    protected void addCaptureMoveExecutors(PiecePositioned origen, PiecePositioned destino, MoveComposed moveImp) {
        if (origen.getPiece().isKing()) {
            moveImp.setFnDoPositionState(algoPositionState::doCaptureKingPositionState);
        } else {
            moveImp.setFnDoPositionState(algoPositionState::doCaptureNotKingPositionState);
        }

        moveImp.setFnDoSquareBoard(algoSquareBoard::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoSquareBoard(algoSquareBoard::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(algoBitBoard::captureFnDoBitBoard);
        moveImp.setFnUndoColorBoard(algoBitBoard::captureFnUndoBitBoard);

        moveImp.setFnDoZobrist(algoZobrist::defaultFnDoZobrist);
    }

}
