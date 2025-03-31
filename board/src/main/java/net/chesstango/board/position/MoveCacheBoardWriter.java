package net.chesstango.board.position;

import net.chesstango.board.Square;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveCacheBoardWriter {
    void setPseudoMoves(Square key, MoveGeneratorByPieceResult generatorResult);

    void affectedPositionsByMove(Square key);

    void affectedPositionsByMove(Square key1, Square key2);

    void affectedPositionsByMove(Square key1, Square key2, Square key3);

    void affectedPositionsByMove(Square key1, Square key2, Square key3, Square key4);

    void push();

    void pop();
}
