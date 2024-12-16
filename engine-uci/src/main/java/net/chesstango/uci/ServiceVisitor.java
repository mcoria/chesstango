package net.chesstango.uci;

import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.proxy.UciProxy;

/**
 * @author Mauricio Coria
 */
public interface ServiceVisitor {

    void visit(UciTango uciTango);

	void visit(UciProxy uciProxy);
}
