package net.chesstango.search.smart.features.debug.traps;

import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.debug.DebugNodeTrap;

import java.io.PrintStream;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class ComposedTrap implements DebugNodeTrap {
    private final Predicate<DebugNode> predicate;

    private final BiConsumer<DebugNode, PrintStream> action;

    public ComposedTrap(Predicate<DebugNode> predicate, BiConsumer<DebugNode, PrintStream> action) {
        this.predicate = predicate;
        this.action = action;
    }

    @Override
    public boolean test(DebugNode debugNode) {
        return predicate.test(debugNode);
    }

    @Override
    public void debugAction(DebugNode debugNode, PrintStream debugOut) {
        action.accept(debugNode, debugOut);
    }
}
