package net.chesstango.search;

/**
 * @author Mauricio Coria
 */
public interface Acceptor {
    default void accept(Visitor visitor) {
        throw new RuntimeException(String.format("Please implement the accept method in %s", this.getClass().getName()));
    }
}
