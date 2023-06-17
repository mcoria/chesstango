package net.chesstango.board.position;

import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveCacheBoardWriter {
    void setPseudoMoves(Square key, MoveGeneratorResult generatorResult);

    void clearPseudoMoves(Square key, boolean trackCleared);

    void clearPseudoMoves(Square key1, Square key2, boolean trackCleared);

    void clearPseudoMoves(Square key1, Square key2, Square key3, boolean trackCleared);

    void clearPseudoMoves(Square key1, Square key2, Square key3, Square key4, boolean trackCleared);

    void pushCleared();

    void popCleared();
}
