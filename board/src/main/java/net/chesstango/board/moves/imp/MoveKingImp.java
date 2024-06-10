package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.KingSquareWriter;

/**
 * @author Mauricio Coria
 */
public class MoveKingImp extends MoveComposed implements MoveKing {

    public MoveKingImp(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(from, to, direction);
    }

    public MoveKingImp(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    /**
     * This method checks if this move is legal or not.
     *
     * @param filter
     * @return
     */
    @Override
    public boolean isLegalMove(LegalMoveFilter filter) {
        return filter.isLegalMove(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoveKingImp theOther) {
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
