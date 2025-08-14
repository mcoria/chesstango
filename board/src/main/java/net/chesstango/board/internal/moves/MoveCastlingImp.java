package net.chesstango.board.internal.moves;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.internal.GameImp;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public abstract class MoveCastlingImp extends MoveKingImp implements MoveCastling {

    protected final PiecePositioned rookFrom;
    protected final PiecePositioned rookTo;

    public MoveCastlingImp(GameImp gameImp, PiecePositioned kingFrom, PiecePositioned kingTo, PiecePositioned rookFrom, PiecePositioned rookTo) {
        super(gameImp, kingFrom, kingTo);

        this.rookFrom = rookFrom;
        this.rookTo = rookTo;
    }

    @Override
    public void doMove(SquareBoardWriter squareBoard) {
        squareBoard.move(from, to);
        squareBoard.move(rookFrom, rookTo);
    }


    @Override
    public void undoMove(SquareBoardWriter squareBoard) {
        squareBoard.setPosition(from);
        squareBoard.setPosition(to);

        squareBoard.setPosition(rookFrom);
        squareBoard.setPosition(rookTo);
    }

    @Override
    public void doMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.piece(), from.square(), to.square());
        bitBoardWriter.swapPositions(rookFrom.piece(), rookFrom.square(), rookTo.square());
    }

    @Override
    public void undoMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.piece(), to.square(), from.square());
        bitBoardWriter.swapPositions(rookFrom.piece(), rookTo.square(), rookFrom.square());
    }

    @Override
    public void doMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.square(), to.square(), rookFrom.square(), rookTo.square());
        moveCache.push();
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.square(), to.square(), rookFrom.square(), rookTo.square());
        moveCache.pop();
    }

    @Override
    public void doMove(ZobristHash hash) {
        zobristHashSnapshot = hash.takeSnapshot();

        hash.xorPosition(from);
        hash.xorPosition(PiecePositioned.of(to.square(), from.piece()));

        hash.xorPosition(rookFrom);
        hash.xorPosition(PiecePositioned.of(rookTo.square(), rookFrom.piece()));

        PositionReader positionReader = gameImp.getPosition();

        xorCastling(hash, positionStateSnapshot, positionReader);

        hash.clearEnPassantSquare();

        hash.xorTurn();
    }

    @Override
    public boolean isLegalMove(LegalMoveFilter filter) {
        return filter.isLegalMoveCastling(this, this);
    }

    @Override
    public String toString() {
        return getFrom().toString() + " " + getTo().toString() + " - " + this.getClass().getSimpleName();
    }

    @Override
    public Cardinal getMoveDirection() {
        return null;
    }

    protected abstract void xorCastling(ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState);
}
