package net.chesstango.uci.engine;

import net.chesstango.uci.engine.states.UciTango;
import net.chesstango.uci.engine.proxy.UciProxy;

/**
 * @author Mauricio Coria
 */
public interface ServiceVisitor {

    void visit(UciTango uciTango);

	void visit(UciProxy uciProxy);
}
