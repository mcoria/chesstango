package net.chesstango.board.moves.generators.legal.imp.check;

import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.moves.imp.MoveImp;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.KingSquare;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.SquareBoard;

/**
 * Este filtro se utiliza cuando el jugador actual SI se encuentra en jaque
 *
 * @author Mauricio Coria
 */
public class CheckLegalMoveFilter implements LegalMoveFilter {

    protected final SquareBoard dummySquareBoard;
    protected final KingSquare kingCacheBoard;
    protected final BitBoard bitBoard;
    protected final PositionStateReader positionState;
    protected final FullScanSquareCaptured fullScanSquareCapturer;

    public CheckLegalMoveFilter(SquareBoard dummySquareBoard, KingSquare kingCacheBoard, BitBoard bitBoard, PositionStateReader positionState) {
        this.dummySquareBoard = dummySquareBoard;
        this.kingCacheBoard = kingCacheBoard;
        this.bitBoard = bitBoard;
        this.positionState = positionState;
        this.fullScanSquareCapturer = new FullScanSquareCaptured(dummySquareBoard, bitBoard);
    }

    @Override
    public boolean isLegalMove(Move move) {
        boolean result = false;

        final Color currentTurn = positionState.getCurrentTurn();

        move.doMove(this.dummySquareBoard);
        move.doMove(this.bitBoard);

        if (!fullScanSquareCapturer.isCaptured(currentTurn.oppositeColor(), kingCacheBoard.getKingSquare(currentTurn))) {
            result = true;
        }

        move.undoMove(this.bitBoard);
        move.undoMove(this.dummySquareBoard);

        return result;
    }

    @Override
    public boolean isLegalMoveKing(MoveKing move) {
        move.doMove(this.kingCacheBoard);

        boolean result = isLegalMove((MoveImp) move);

        move.undoMove(this.kingCacheBoard);

        return result;
    }

    @Override
    public boolean isLegalMoveCastling(MoveCastling moveCastling) {
        return false;
    }

}
