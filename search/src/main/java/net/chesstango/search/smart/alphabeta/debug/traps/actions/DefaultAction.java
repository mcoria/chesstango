package net.chesstango.search.smart.alphabeta.debug.traps.actions;

import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;

import java.io.PrintStream;
import java.util.function.BiConsumer;

/**
 * @author Mauricio Coria
 */
public class DefaultAction implements BiConsumer<DebugNode, PrintStream> {

    @Override
    public void accept(DebugNode debugNode, PrintStream printStream) {
        printStream.print("ACA HAY UNA ENTRADA\n");
    }
}

