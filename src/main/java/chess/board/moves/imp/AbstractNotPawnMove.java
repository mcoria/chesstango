package chess.board.moves.imp;

import chess.board.PiecePositioned;
import chess.board.iterators.Cardinal;

abstract class AbstractNotPawnMove extends AbstractMove {
    public AbstractNotPawnMove(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(from, to, direction);
    }

    public AbstractNotPawnMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }
}
