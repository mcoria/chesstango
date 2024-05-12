package net.chesstango.board.position;

import net.chesstango.board.Square;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveCacheBoardReader {
    MoveGeneratorResult getPseudoMovesResult(Square key);

    long getPseudoMovesPositions();
}
