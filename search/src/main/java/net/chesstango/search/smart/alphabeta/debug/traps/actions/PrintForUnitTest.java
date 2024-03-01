package net.chesstango.search.smart.alphabeta.debug.traps.actions;


import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Mauricio Coria
 */
public class PrintForUnitTest implements BiConsumer<DebugNode, PrintStream> {

    private final HexFormat hexFormat = HexFormat.of().withUpperCase();

    @Override
    public void accept(DebugNode debugNode, PrintStream printStream) {
        printStream.println("=======================");
        printGame(debugNode, printStream);
        printTTContext(debugNode, printStream);
        printCacheContext(debugNode, printStream);
        printStream.println("=======================");
    }

    private void printTTContext(DebugNode debugNode, PrintStream printStream) {
        debugNode.getSorterReads()
                .forEach(ttOperation -> {
                    TranspositionEntry entry = ttOperation.getEntry();

                    String table = switch (ttOperation.getTableType()) {
                        case MAX_MAP -> "maxMap";
                        case MIN_MAP -> "minMap";
                        case MAX_MAP_Q -> "qMaxMap";
                        case MIN_MAP_Q -> "qMinMap";
                    };

                    printStream.printf("%s.write(0x%sL, %d, %dL, TranspositionBound.%s); // %s \n",
                            table,
                            hexFormat.formatHex(longToByte(ttOperation.getHashRequested())),
                            entry.getSearchDepth(),
                            entry.getMovesAndValue(),
                            entry.getTranspositionBound(),
                            ttOperation.getMove()
                    );

                });
        printStream.println("\n");
    }

    private void printCacheContext(DebugNode debugNode, PrintStream printStream) {
        debugNode.getEvalCacheReads()
                .forEach(cacheRead -> printStream.printf("cacheEvaluation.put(0x%sL, %d); // %s \n",
                        hexFormat.formatHex(longToByte(cacheRead.getHashRequested())),
                        cacheRead.getEvaluation(),
                        cacheRead.getMove()));
        printStream.println("\n");
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

        printStream.printf("game = FENDecoder.loadGame(\"" + tree.get(0).getFen() + "\")");
        tree.forEach(node -> {
            Move move = node.getSelectedMove();
            if (move != null) {
                printStream.printf("\n.executeMove(Square." + move.getFrom().getSquare().toString() + ", Square." + move.getTo().getSquare().toString() + ")");
            }
        });
        printStream.println(";\n");
    }

    private byte[] longToByte(long lng) {
        return new byte[]{
                (byte) (lng >> 56),
                (byte) (lng >> 48),
                (byte) (lng >> 40),
                (byte) (lng >> 32),
                (byte) (lng >> 24),
                (byte) (lng >> 16),
                (byte) (lng >> 8),
                (byte) lng
        };
    }
}
