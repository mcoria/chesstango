package net.chesstango.board.internal.moves.factories;

import net.chesstango.board.internal.GameImp;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.internal.moves.*;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.MoveFactory;

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
    public MoveComposed createSimpleKnightMove(PiecePositioned from, PiecePositioned to) {
        MoveComposed move = new MoveComposed(gameImp, from, to);
        addSimpleMoveExecutors(from, to, move);
        move.setFnDoFilterMove(legalMoveFilter -> legalMoveFilter.isLegalMoveKnight(move, move));
        return move;
    }

    @Override
    public MoveComposed createSimpleBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        MoveComposed move = createSimpleMoveImp(from, to, cardinal);
        move.setFnDoFilterMove(legalMoveFilter -> legalMoveFilter.isLegalMoveBishop(move, move));
        return move;
    }

    @Override
    public MoveComposed createSimpleQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        MoveComposed move = createSimpleMoveImp(from, to, cardinal);
        move.setFnDoFilterMove(legalMoveFilter -> legalMoveFilter.isLegalMoveQueen(move, move));
        return move;
    }

    @Override
    public MoveComposed createSimpleRookMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        MoveComposed move = new MoveComposed(gameImp, from, to);
        addSimpleMoveExecutors(from, to, move);
        move.setFnDoFilterMove(legalMoveFilter -> legalMoveFilter.isLegalMoveRook(move, move));
        return move;
    }

    @Override
    public MoveKingImp createSimpleKingMove(PiecePositioned from, PiecePositioned to) {
        MoveKingImp moveImp = new MoveKingImp(gameImp, from, to);
        addSimpleMoveExecutors(from, to, moveImp);
        return moveImp;
    }

    @Override
    public MoveComposed createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to) {
        MoveComposed move = new MoveComposed(gameImp, from, to, getPawnDirection());
        addSimpleMoveExecutors(from, to, move);
        move.setFnDoFilterMove(legalMoveFilter -> legalMoveFilter.isLegalMovePawn(move, move));
        return move;
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
    public MoveComposed createCaptureKnightMove(PiecePositioned from, PiecePositioned to) {
        MoveComposed move = new MoveComposed(gameImp, from, to);
        addCaptureMoveExecutors(from, to, move);
        move.setFnDoFilterMove(legalMoveFilter -> legalMoveFilter.isLegalMoveKnight(move, move));
        return move;
    }

    @Override
    public MoveComposed createCaptureBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        MoveComposed move = createCaptureMoveImp(from, to, cardinal);
        move.setFnDoFilterMove(legalMoveFilter -> legalMoveFilter.isLegalMoveBishop(move, move));
        return move;
    }

    @Override
    public MoveComposed createCaptureQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        MoveComposed move = createCaptureMoveImp(from, to, cardinal);
        move.setFnDoFilterMove(legalMoveFilter -> legalMoveFilter.isLegalMoveQueen(move, move));
        return move;
    }

    @Override
    public MoveComposed createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        MoveComposed move = new MoveComposed(gameImp, from, to, cardinal);
        addCaptureMoveExecutors(from, to, move);
        move.setFnDoFilterMove(legalMoveFilter -> legalMoveFilter.isLegalMovePawn(move, move));
        return move;
    }

    @Override
    public MoveComposed createCaptureRookMove(PiecePositioned form, PiecePositioned to, Cardinal cardinal) {
        MoveComposed move = new MoveComposed(gameImp, form, to, cardinal);
        addCaptureMoveExecutors(form, to, move);
        move.setFnDoFilterMove(legalMoveFilter -> legalMoveFilter.isLegalMoveRook(move, move));
        return move;
    }

    @Override
    public MoveKingImp createCaptureKingMove(PiecePositioned from, PiecePositioned to) {
        MoveKingImp move = new MoveKingImp(gameImp, from, to);
        addCaptureMoveExecutors(from, to, move);
        move.setFnDoFilterMove(legalMoveFilter -> legalMoveFilter.isLegalMoveKing(move, move));
        return move;
    }

    @Override
    public MoveCaptureEnPassantImp createCaptureEnPassantPawnMove(PiecePositioned from, PiecePositioned to, PiecePositioned enPassantPawn, Cardinal cardinal) {
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

    public MoveComposed createSimpleMoveImp(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        MoveComposed moveImp = new MoveComposed(gameImp, origen, destino, cardinal);
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    protected MoveComposed createCaptureMoveImp(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
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

        moveImp.setFnDoBitBoard(algoBitBoard::defaultFnDoBitBoard);
        moveImp.setFnUndoBitBoard(algoBitBoard::defaultFnUndoBitBoard);

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

        moveImp.setFnDoBitBoard(algoBitBoard::captureFnDoBitBoard);
        moveImp.setFnUndoBitBoard(algoBitBoard::captureFnUndoBitBoard);

        moveImp.setFnDoZobrist(algoZobrist::defaultFnDoZobrist);
    }
}
