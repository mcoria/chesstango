package net.chesstango.board.position;

import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveCacheBoardWriter {
    void setPseudoMoves(Square key, MoveGeneratorResult generatorResult);

    void clearPseudoMoves(Square key);

    void clearPseudoMoves(Square key1, Square key2);

    void clearPseudoMoves(Square key1, Square key2, Square key3);

    void clearPseudoMoves(Square key1, Square key2, Square key3, Square key4);

    void push();

    void pop();
}
