package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.MoveCacheBoardWriter;
import net.chesstango.board.position.PositionStateWriter;
import net.chesstango.board.position.ZobristHashWriter;

/**
 * @author Mauricio Coria
 */
public abstract class MoveImp implements Move {
    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final Cardinal direction;

    public MoveImp(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        /*
        if (direction != null && !direction.equals(Cardinal.calculateSquaresDirection(from.getSquare(), to.getSquare()))) {
            throw new RuntimeException(String.format("Direccion %s however %s %s %s", direction, Cardinal.calculateSquaresDirection(from.getSquare(), to.getSquare()), from, to));
        }
         */
        this.from = from;
        this.to = to;
        this.direction = direction;
        assert (direction == null || direction.equals(Cardinal.calculateSquaresDirection(from.getSquare(), to.getSquare())));
    }

    public MoveImp(PiecePositioned from, PiecePositioned to) {
        this.from = from;
        this.to = to;
        this.direction = calculateMoveDirection();
    }

    @Override
    public PiecePositioned getFrom() {
        return from;
    }

    @Override
    public PiecePositioned getTo() {
        return to;
    }

    @Override
    public void undoMove(PositionStateWriter positionStateWriter) {
        positionStateWriter.popState();
    }

    @Override
    public void doMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare());
        moveCache.push();
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare());
        moveCache.pop();
    }

    @Override
    public void undoMove(ZobristHashWriter hash) {
        hash.popState();
    }

    @Override
    public Cardinal getMoveDirection() {
        return direction;
    }

    @Override
    public boolean isQuiet() {
        return to.getPiece() == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoveImp theOther) {
            return from.equals(theOther.from) && to.equals(theOther.to);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s", from, to, getClass().getSimpleName());
    }

    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().getPiece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
    }
}
