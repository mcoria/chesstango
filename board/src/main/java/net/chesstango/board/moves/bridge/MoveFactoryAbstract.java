package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.PiecePlacementWriter;
import net.chesstango.board.position.imp.ColorBoard;

/**
 * @author Mauricio Coria
 *
 */
public abstract class MoveFactoryAbstract  implements MoveFactory {

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

        moveImp.setFnDoMovePiecePlacement(MoveFactoryAbstract::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(MoveFactoryAbstract::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(MoveFactoryAbstract::defaultFnDoColorBoard);
        moveImp.setFnUndoColorBoard(MoveFactoryAbstract::defaultFnUndoColorBoard);
    }

    protected void addSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, MoveImp moveImp, Square enPassantSquare) {
        moveImp.setFnUpdatePositionStateBeforeRollTurn(positionState -> {
            positionState.resetHalfMoveClock();
            positionState.setEnPassantSquare(enPassantSquare);
        });

        moveImp.setFnDoMovePiecePlacement(MoveFactoryAbstract::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(MoveFactoryAbstract::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(MoveFactoryAbstract::defaultFnDoColorBoard);
        moveImp.setFnUndoColorBoard(MoveFactoryAbstract::defaultFnUndoColorBoard);
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

        moveImp.setFnDoMovePiecePlacement(MoveFactoryAbstract::defaultFnDoMovePiecePlacement);
        moveImp.setFnUndoMovePiecePlacement(MoveFactoryAbstract::defaultFnUndoMovePiecePlacement);

        moveImp.setFnDoColorBoard(MoveFactoryAbstract::captureFnDoColorBoard);
        moveImp.setFnUndoColorBoard(MoveFactoryAbstract::captureFnUndoColorBoard);
    }

    protected static void captureFnDoColorBoard(PiecePositioned from, PiecePositioned to, ColorBoard colorBoard) {
        colorBoard.removePositions(to);

        colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());
    }

    protected static void captureFnUndoColorBoard(PiecePositioned from, PiecePositioned to, ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());

        colorBoard.addPositions(to);
    }

    protected static void defaultFnDoColorBoard(PiecePositioned from, PiecePositioned to, ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());
    }

    protected static void defaultFnUndoColorBoard(PiecePositioned from, PiecePositioned to, ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());
    }

    protected static void defaultFnDoMovePiecePlacement(PiecePositioned from, PiecePositioned to, PiecePlacementWriter piecePlacementWriter) {
        piecePlacementWriter.move(from, to);
    }

    protected static void defaultFnUndoMovePiecePlacement(PiecePositioned from, PiecePositioned to, PiecePlacementWriter piecePlacementWriter) {
        piecePlacementWriter.setPosicion(from);
        piecePlacementWriter.setPosicion(to);
    }

}
