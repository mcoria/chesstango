package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;

abstract class AbstractNotPawnMove extends AbstractMove {
    public AbstractNotPawnMove(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(from, to, direction);
    }

    public AbstractNotPawnMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }
}
