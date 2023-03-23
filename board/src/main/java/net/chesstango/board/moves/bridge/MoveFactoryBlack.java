package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryBlack extends MoveFactoryAbstract{

    private static final MoveCastling castlingKingMove = new CastlingBlackKingMove();
    private static final MoveCastling castlingQueenMove = new CastlingBlackQueenMove();

    @Override
    public Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino) {
        MoveImp moveImp = new MoveImp(origen, destino, Cardinal.Sur);
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare) {
        MoveImp moveImp = new MoveImp(origen, destino, Cardinal.Sur);
        addSimpleTwoSquaresPawnMove(origen, destino, moveImp, enPassantSquare);
        return moveImp;
    }

    @Override
    public MovePromotion createSimplePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        MovePromotionImp moveImp = new MovePromotionImp(origen, destino, Cardinal.Sur, piece);
        moveImp.setFnDoColorBoard(algoColorBoard::defaultFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::defaultFnUndoColorBoard);
        return moveImp;
    }

    @Override
    public Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return null;
    }

    @Override
    public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return null;
    }

    @Override
    public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return null;
    }

    @Override
    public MoveCastling createCastlingQueenMove() {
        return castlingQueenMove;
    }

    @Override
    public MoveCastling createCastlingKingMove() {
        return castlingKingMove;
    }

    @Override
    protected void fnKingUpdatePositionStateBeforeRollTurn(PositionState positionState) {
        this.algoPositionState.kingBlackUpdatePositionStateBeforeRollTurn(positionState);
    }

    @Override
    protected void fnKingCaptureUpdatePositionStateBeforeRollTurn(PositionState positionState) {
        this.algoPositionState.kingBlackCaptureUpdatePositionStateBeforeRollTurn(positionState);
    }

    @Override
    protected void fnDoZobritKing(PiecePositioned from, PiecePositioned to, ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        this.alogZobrit.fnDoZobritKingBlack(from, to, hash, oldPositionState, newPositionState);
    }

    @Override
    protected void fnPawnCaptureUpdatePositionStateBeforeRollTurn(PositionState positionState) {
        this.algoPositionState.pawnBlackCaptureUpdatePositionStateBeforeRollTurn(positionState);
    }
}
