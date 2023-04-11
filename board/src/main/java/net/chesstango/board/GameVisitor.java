package net.chesstango.board;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface GameVisitor {
    void visit(GameStateReader gameState);

}
