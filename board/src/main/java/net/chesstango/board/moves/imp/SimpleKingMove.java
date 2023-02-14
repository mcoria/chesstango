package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.MoveKing;

/**
 * @author Mauricio Coria
 */
public class SimpleKingMove extends SimpleMove implements MoveKing {

    public SimpleKingMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof SimpleKingMove;
    }

}
