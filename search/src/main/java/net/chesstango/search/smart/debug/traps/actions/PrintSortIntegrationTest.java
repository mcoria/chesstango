package net.chesstango.search.smart.debug.traps.actions;


import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class PrintSortIntegrationTest implements Consumer<DebugNode> {

    private final HexFormat hexFormat = HexFormat.of().withUpperCase();

    @Override
    public void accept(DebugNode debugNode) {
        if (debugNode.getSortedMoves() != null) {
            PrintStream printStream = System.out;
            printStream.println("=======================");
            printGame(debugNode, printStream);
            printTTContext(debugNode, printStream);
            printCacheContext(debugNode, printStream);
            printKmContext(debugNode, printStream);
            printSortedMoves(debugNode, printStream);
            printStream.println("=======================");
        }
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

        printStream.printf("Game game = Game.from(FEN.from(\"" + tree.getFirst().getFen() + "\"))%n");
        tree.forEach(node -> {
            Move move = node.getSelectedMove();
            if (move != null) {
                printStream.printf("\t.executeMove(Square.%s,Square.%s)%n", move.getFrom().square(), move.getTo().square());
            }
        });
        printStream.println();
    }

    private void printTTContext(DebugNode debugNode, PrintStream printStream) {
        debugNode.getSorterReads()
                .forEach(ttOperation -> {
                    TranspositionEntry entry = ttOperation.getEntry();
                    printStream.printf("ttWrite(0x%sL, (byte) %d, (short) %d, %d, %s); // %s \n",
                            hexFormat.formatHex(longToByte(entry.getHash())),
                            entry.getDraft(),
                            entry.getMove(),
                            entry.getValue(),
                            entry.getBound(),
                            ttOperation.getSortingMove()
                    );
                });
        printStream.println();
    }

    private void printCacheContext(DebugNode debugNode, PrintStream printStream) {
        debugNode.getEvalCacheReads()
                .forEach(cacheRead -> printStream.printf("cacheEvaluationWrite(0x%sL, %d); // %s \n",
                        hexFormat.formatHex(longToByte(cacheRead.getHashRequested())),
                        cacheRead.getEvaluation(),
                        cacheRead.getMove()));

        printStream.println();
    }

    private void printKmContext(DebugNode debugNode, PrintStream printStream) {
        if (debugNode.getKillerMovesTableA() != null) {
            String moveStr = debugNode.getKillerMovesTableA().coordinateEncoding();
            printStream.printf("killerMoves.trackKillerMove(getMove(game, \"%s\"), %d); // %s%n", moveStr, debugNode.getPly() + 1, moveStr);
        }

        if (debugNode.getKillerMovesTableB() != null) {
            String moveStr = debugNode.getKillerMovesTableB().coordinateEncoding();
            printStream.printf("killerMoves.trackKillerMove(getMove(game, \"%s\"), %d); // %s%n", moveStr, debugNode.getPly() + 1, moveStr);
        }
        printStream.println();
    }

    private void printSortedMoves(DebugNode debugNode, PrintStream printStream) {
        printStream.printf("List<String> actualSort = toMoveStrList(moveSorterInterior.getOrderedMoves(%d));%n", debugNode.getPly());

        printStream.printf("assertEquals(List.of(%s), actualSort);%n",
                debugNode.getSortedMoves()
                        .stream()
                        .map(moveStr -> "\"" + moveStr + "\"")
                        .reduce((a, b) -> a + ", " + b).orElse("")
        );

        printStream.println();
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
