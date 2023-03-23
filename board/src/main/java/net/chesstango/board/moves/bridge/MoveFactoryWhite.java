package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryWhite extends  MoveFactoryAbstract{

    private static final MoveCastling castlingKingMove = new CastlingWhiteKingMove();
    private static final MoveCastling castlingQueenMove = new CastlingWhiteQueenMove();

    public MoveFactoryWhite() {
        super(new AlgoPositionStateWhite());
    }

    @Override
    public Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino) {
        MoveImp moveImp = new MoveImp(origen, destino, Cardinal.Norte);
        addSimpleMoveExecutors(origen, destino, moveImp);
        return moveImp;
    }

    @Override
    public Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare) {
        MovePawnTwoSquares moveImp = new MovePawnTwoSquares(origen, destino, Cardinal.Norte, enPassantSquare);
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
    public MoveCastling createCastlingQueenMove() {
        return castlingQueenMove;
    }

    @Override
    public MoveCastling createCastlingKingMove() {
        return castlingKingMove;
    }


}
