package net.chesstango.uci.service;

/**
 * @author Mauricio Coria
 */
public interface ServiceElement {
    void accept(ServiceVisitor serviceVisitor);
}
