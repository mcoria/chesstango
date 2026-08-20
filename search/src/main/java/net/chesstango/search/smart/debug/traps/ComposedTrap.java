package net.chesstango.search.smart.debug.traps;

import net.chesstango.search.smart.debug.DebugNodeTrap;
import net.chesstango.search.smart.debug.model.DebugNode;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class ComposedTrap implements DebugNodeTrap {
    private final Predicate<DebugNode> predicate;

    private final Consumer<DebugNode> action;

    public ComposedTrap(Predicate<DebugNode> predicate, Consumer<DebugNode> action) {
        this.predicate = predicate;
        this.action = action;
    }

    @Override
    public boolean test(DebugNode debugNode) {
        return predicate.test(debugNode);
    }

    @Override
    public void debugAction(DebugNode debugNode) {
        action.accept(debugNode);
    }
}
