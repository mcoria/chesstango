package net.chesstango.board.position;

import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveCacheBoardReader {
    MoveGeneratorResult getPseudoMovesResult(Square key);

    long getClearedSquares();
}
