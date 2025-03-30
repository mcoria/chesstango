package net.chesstango.board.moves.generators.legal;

import net.chesstango.board.moves.imp.MoveCastlingImp;
import net.chesstango.board.moves.imp.MoveImp;
import net.chesstango.board.moves.imp.MoveKingImp;

/**
 * @author Mauricio Coria
 */
public interface LegalMoveFilter {
    boolean isLegalMove(MoveImp move);

    boolean isLegalMoveKing(MoveKingImp move);

    boolean isLegalMoveCastling(MoveCastlingImp moveCastling);
}
