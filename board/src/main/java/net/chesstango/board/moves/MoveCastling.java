package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;

/**
 * @author Mauricio Coria
 */

public interface MoveCastling extends MoveKing {

    PiecePositioned getRookFrom();

    PiecePositioned getRookTo();

}
