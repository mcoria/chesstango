package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveKing;

/**
 * @author Mauricio Coria
 */
class MoveKingImp extends MoveImp implements MoveKing {

    public MoveKingImp(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(from, to, direction);
    }

    public MoveKingImp(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

}
