package net.chesstango.board.moves.generators.legal.imp.check;

import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveCommand;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.moves.imp.MoveCastlingImp;
import net.chesstango.board.moves.imp.MoveImp;
import net.chesstango.board.moves.imp.MoveKingImp;
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

    protected final SquareBoard squareBoard;
    protected final KingSquare kingCacheBoard;
    protected final BitBoard bitBoard;
    protected final PositionStateReader positionState;
    protected final FullScanSquareCaptured fullScanSquareCapturer;

    public CheckLegalMoveFilter(SquareBoard squareBoard, KingSquare kingCacheBoard, BitBoard bitBoard, PositionStateReader positionState) {
        this.squareBoard = squareBoard;
        this.kingCacheBoard = kingCacheBoard;
        this.bitBoard = bitBoard;
        this.positionState = positionState;
        this.fullScanSquareCapturer = new FullScanSquareCaptured(squareBoard, bitBoard);
    }

    @Override
    public boolean isLegalMove(Move move, MoveCommand command) {
        boolean result = false;

        final Color currentTurn = positionState.getCurrentTurn();

        command.doMove(this.squareBoard);
        command.doMove(this.bitBoard);

        if (!fullScanSquareCapturer.isCaptured(currentTurn.oppositeColor(), kingCacheBoard.getKingSquare(currentTurn))) {
            result = true;
        }

        command.undoMove(this.bitBoard);
        command.undoMove(this.squareBoard);

        return result;
    }

    @Override
    public boolean isLegalMoveKing(Move move, MoveCommand command) {
        command.doMove(this.kingCacheBoard);

        boolean result = isLegalMove(move, command);

        command.undoMove(this.kingCacheBoard);

        return result;
    }

    @Override
    public boolean isLegalMoveCastling(MoveCastling move, MoveCommand command) {
        return false;
    }

}
