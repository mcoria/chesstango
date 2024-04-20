package net.chesstango.board;

import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 */
public interface GameVisitor {
    void visit(ChessPositionReader chessPositionReader);

    void visit(GameStateReader gameState);

    void visit(MoveGenerator moveGenerator);

}
