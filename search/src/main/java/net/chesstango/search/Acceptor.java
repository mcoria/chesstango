package net.chesstango.search;

/**
 * Defines the Acceptor role in the Visitor design pattern implementation.
 * <p>
 * This interface allows objects to accept visitors that can traverse and operate on
 * the search algorithm's component hierarchy. Components implementing this interface
 * can be visited by various visitors to perform operations such as configuration,
 * inspection, or modification without changing the component classes themselves.
 * </p>
 * <p>
 * The Visitor pattern is extensively used in this search engine to:
 * <ul>
 *   <li>Configure search depth across multiple components (see {@link net.chesstango.search.visitors.SetDepthVisitor})</li>
 *   <li>Set maximum search depth limits (see {@link net.chesstango.search.visitors.SetMaxDepthVisitor})</li>
 *   <li>Traverse and inspect the component chain structure</li>
 *   <li>Apply cross-cutting concerns like statistics collection and debugging</li>
 * </ul>
 * </p>
 * <p>
 * Implementations should override the {@link #accept(Visitor)} method to allow
 * the visitor to perform its operation and, if necessary, propagate the visit
 * to child components in the hierarchy.
 * </p>
 *
 * @author Mauricio Coria
 * @see Visitor
 */
public interface Acceptor {
    /**
     * Accepts a visitor and allows it to perform operations on this component.
     * <p>
     * The default implementation throws a {@link RuntimeException} to enforce
     * that concrete implementations must provide their own accept logic.
     * Implementations typically call the appropriate visit method on the visitor
     * and may propagate the visitor to child components.
     * </p>
     *
     * @param visitor the visitor to accept
     * @throws RuntimeException if the accept method is not properly implemented
     */
    default void accept(Visitor visitor) {
        throw new RuntimeException(String.format("Please implement the accept method in %s", this.getClass().getName()));
    }
}
