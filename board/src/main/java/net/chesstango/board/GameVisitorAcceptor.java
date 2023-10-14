package net.chesstango.board;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface GameVisitorAcceptor {
    void accept(GameVisitor visitor);
}
