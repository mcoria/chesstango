package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.KingSquareWriter;

/**
 * @author Mauricio Coria
 */
class MoveKing extends MoveComposed implements net.chesstango.board.moves.MoveKing {

    public MoveKing(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(from, to, direction);
    }

    public MoveKing(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoveKing theOther) {
            return from.equals(theOther.from) && to.equals(theOther.to);
        }
        return false;
    }

    @Override
    public void doMove(KingSquareWriter kingSquareWriter) {
        kingSquareWriter.setKingSquare(getFrom().getPiece().getColor(), getTo().getSquare());
    }

    @Override
    public void undoMove(KingSquareWriter kingSquareWriter) {
        kingSquareWriter.setKingSquare(getFrom().getPiece().getColor(), getFrom().getSquare());
    }
}
