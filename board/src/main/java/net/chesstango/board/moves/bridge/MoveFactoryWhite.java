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
public class MoveFactoryWhite extends  MoveFactoryAbstract{

    private static final MoveCastling castlingKingMove = new CastlingWhiteKingMove();
    private static final MoveCastling castlingQueenMove = new CastlingWhiteQueenMove();

    @Override
    public Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino) {
        MoveImp moveImp = new MoveImp(origen, destino, Cardinal.Norte);
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare) {
        MoveImp moveImp = new MoveImp(origen, destino, Cardinal.Norte);
        addSimpleTwoSquaresPawnMove(origen, destino, moveImp, enPassantSquare);
        return moveImp;
    }

    @Override
    public MovePromotion createSimplePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
        MovePromotionImp moveImp = new MovePromotionImp(origen, destino, Cardinal.Norte, piece);
        moveImp.setFnDoColorBoard(algoColorBoard::defaultFnDoColorBoard);
        moveImp.setFnUndoColorBoard(algoColorBoard::defaultFnUndoColorBoard);
        return moveImp;
    }

    @Override
    public Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return null;
    }

    @Override
    public Move createCaptureEnPassant(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal, PiecePositioned capture) {
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
        this.algoPositionState.kingWhiteUpdatePositionStateBeforeRollTurn(positionState);
    }

    @Override
    protected void fnKingCaptureUpdatePositionStateBeforeRollTurn(PositionState positionState) {
        this.algoPositionState.kingWhiteCaptureUpdatePositionStateBeforeRollTurn(positionState);
    }

    @Override
    protected void fnDoZobritKing(PiecePositioned from, PiecePositioned to, ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        this.alogZobrit.fnDoZobritKingWhite(from, to, hash, oldPositionState, newPositionState);
    }
}
