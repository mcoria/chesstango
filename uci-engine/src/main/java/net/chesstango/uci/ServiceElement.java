package net.chesstango.uci;

/**
 * @author Mauricio Coria
 */
public interface ServiceElement {
    void accept(ServiceVisitor serviceVisitor);
}
