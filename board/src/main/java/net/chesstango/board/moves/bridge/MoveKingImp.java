package net.chesstango.board.moves.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveKing;

/**
 * @author Mauricio Coria
 */
public class MoveKingImp extends MoveImp implements MoveKing {

    public MoveKingImp(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(from, to, direction);
    }

    public MoveKingImp(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

}
