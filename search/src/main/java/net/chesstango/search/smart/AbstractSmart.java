package net.chesstango.search.smart;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.search.SearchMove;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractSmart implements SearchMove {
    protected boolean keepProcessing = true;

    @Override
    public void stopSearching() {
        keepProcessing = false;
    }

}
