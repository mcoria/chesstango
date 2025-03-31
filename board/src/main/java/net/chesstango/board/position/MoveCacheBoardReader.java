package net.chesstango.board.position;

import net.chesstango.board.Square;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveCacheBoardReader {
    MoveGeneratorByPieceResult getPseudoMovesResult(Square key);

    long getPseudoMovesPositions();
}
