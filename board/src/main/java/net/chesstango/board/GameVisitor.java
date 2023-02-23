package net.chesstango.board;

/**
 * @author Mauricio Coria
 */
public interface GameVisitor {
    void visit(GameState gameState);

    void visit(GameState.GameStateData gameStateData);
}
