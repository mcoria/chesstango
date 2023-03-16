package net.chesstango.uci.service;

import net.chesstango.engine.Session;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.proxy.EngineProxy;

/**
 * @author Mauricio Coria
 */
public interface ServiceVisitor {
    void visit(EngineController engineController);

    void visit(EngineTango engineTango);

    void visit(EngineProxy engineProxy);

    void visit(Tango tango);

    void visit(Session session);
}
