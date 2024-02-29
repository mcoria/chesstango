package net.chesstango.search.smart.alphabeta.debug.traps.actions;


import net.chesstango.search.smart.alphabeta.debug.DebugNode;

import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Mauricio Coria
 */
public class PrintForUnitTest implements BiConsumer<DebugNode, PrintStream> {

    @Override
    public void accept(DebugNode debugNode, PrintStream printStream) {
        printGame(debugNode, printStream);
    }

    private void printGame(DebugNode debugNode, PrintStream printStream) {
        List<DebugNode> tree = new LinkedList<>();
        tree.add(debugNode);

        DebugNode parentNode = debugNode.getParent();
        while (parentNode != null) {
            tree.add(parentNode);
            parentNode = parentNode.getParent();
        }

        Collections.reverse(tree);

        
    }
}
