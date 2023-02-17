package net.chesstango.board;

/**
 * @author Mauricio Coria
 */
public interface GameStateVisitor {
    void visit(GameState gameState);

    void visit(GameState.GameStateData gameStateData);
}
