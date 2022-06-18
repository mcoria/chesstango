package chess.board.moves.imp;

import chess.board.PiecePositioned;
import chess.board.iterators.Cardinal;

abstract class AbstractPawnMove extends AbstractMove {
    public AbstractPawnMove(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(from, to, direction);
    }

    public AbstractPawnMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }
}
