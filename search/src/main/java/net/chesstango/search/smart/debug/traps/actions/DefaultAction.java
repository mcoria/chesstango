package net.chesstango.search.smart.debug.traps.actions;

import net.chesstango.search.smart.debug.model.DebugNode;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class DefaultAction implements Consumer<DebugNode> {

    @Override
    public void accept(DebugNode debugNode) {
        System.out.print("ACA HAY UNA ENTRADA\n");
    }
}

