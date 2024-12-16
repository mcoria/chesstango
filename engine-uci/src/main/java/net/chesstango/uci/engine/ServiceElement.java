package net.chesstango.uci.engine;

/**
 * @author Mauricio Coria
 */
public interface ServiceElement {
    void accept(ServiceVisitor serviceVisitor);
}
