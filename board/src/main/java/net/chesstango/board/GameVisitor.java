package net.chesstango.board;

/**
 * @author Mauricio Coria
 */
public interface GameVisitor {
    void visit(GameStateReader gameState);

}
