package net.chesstango.board.moves.factories.imp;

import net.chesstango.board.GameImp;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.imp.*;

/**
 * @author Mauricio Coria
 */
public abstract class MoveFactoryAbstract implements MoveFactory {
    protected final AlgoSquareBoard algoSquareBoard = new AlgoSquareBoard();

    protected final AlgoBitBoard algoBitBoard = new AlgoBitBoard();

    protected final AlgoZobrist algoZobrist = new AlgoZobrist();

    protected final AlgoPositionState algoPositionState;

    protected final GameImp gameImp;

    protected abstract Cardinal getPawnDirection();

    protected MoveFactoryAbstract(GameImp gameImp, AlgoPositionState algoPositionState) {
        this.gameImp = gameImp;
        this.algoPositionState = algoPositionState;
    }

    /*******************************************************************************
     * SIMPLE MOVES
     *
     */
    @Override
    public MoveImp createSimpleKnightMove(PiecePositioned from, PiecePositioned to) {
        MoveComposed moveImp = new MoveComposed(gameImp, from, to);
        addSimpleMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public MoveImp createSimpleBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return createSimpleMoveImp(from, to, cardinal);
    }

    @Override
    public MoveImp createSimpleQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return createSimpleMoveImp(from, to, cardinal);
    }

    @Override
    public MoveImp createSimpleRookMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        MoveComposed moveImp = new MoveComposed(gameImp, from, to);
        addSimpleMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public MoveKingImp createSimpleKingMove(PiecePositioned from, PiecePositioned to) {
        MoveKingImp moveImp = new MoveKingImp(gameImp, from, to);
        addSimpleMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public MoveImp createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to) {
        MoveComposed moveImp = new MoveComposed(gameImp, from, to, getPawnDirection());
        addSimpleMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public MoveImp createSimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to, Square enPassantSquare) {
        return new MovePawnTwoSquares(gameImp, from, to, getPawnDirection(), enPassantSquare);
    }

    @Override
    public MovePromotionImp createSimplePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece) {
        return new MovePromotionImp(gameImp, from, to, getPawnDirection(), piece);
    }

    /*******************************************************************************
     * CAPTURE MOVES
     *
     */

    @Override
    public MoveImp createCaptureKnightMove(PiecePositioned from, PiecePositioned to) {
        MoveComposed moveImp = new MoveComposed(gameImp, from, to);
        addCaptureMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public MoveImp createCaptureBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return createCaptureMoveImp(from, to, cardinal);
    }

    @Override
    public MoveImp createCaptureQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return createCaptureMoveImp(from, to, cardinal);
    }

    @Override
    public MoveImp createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        MoveComposed moveImp = new MoveComposed(gameImp, from, to, cardinal);
        addCaptureMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public MoveImp createCaptureRookMove(PiecePositioned form, PiecePositioned to, Cardinal cardinal) {
        MoveComposed moveImp = new MoveComposed(gameImp, form, to, cardinal);
        addCaptureMoveExecutors(form, to, moveImp);
        return moveImp;
    }

    @Override
    public MoveKingImp createCaptureKingMove(PiecePositioned from, PiecePositioned to) {
        MoveKingImp moveImp = new MoveKingImp(gameImp, from, to);
        addCaptureMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public MoveImp createCaptureEnPassantPawnMove(PiecePositioned from, PiecePositioned to, PiecePositioned enPassantPawn, Cardinal cardinal) {
        return new MoveCaptureEnPassantImp(gameImp, from, to, cardinal, enPassantPawn);
    }

    @Override
    public MovePromotionImp createCapturePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece, Cardinal cardinal) {
        return new MovePromotionImp(gameImp, from, to, cardinal, piece);
    }

    /*******************************************************************************
     *
     * WIRING
     *
     *
     */

    public MoveImp createSimpleMoveImp(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        MoveComposed moveImp = new MoveComposed(gameImp, origen, destino, cardinal);
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    protected MoveImp createCaptureMoveImp(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        MoveComposed moveImp = new MoveComposed(gameImp, origen, destino, cardinal);
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
