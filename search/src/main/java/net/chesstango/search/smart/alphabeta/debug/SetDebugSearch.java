package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class SetDebugSearch implements SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener {
    private final boolean showOnlyPV;
    private final boolean showNodeTranspositionAccess;
    private final boolean showNodeSorterTranspositionAccess;
    private final boolean withAspirationWindows;
    private final DebugNodeTrap debugNodeTrap;
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault());
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();
    private final HexFormat hexFormat = HexFormat.of().withUpperCase();
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private PrintStream debugOut;
    private SearchTracker searchTracker;
    private List<String> debugErrorMessages;

    public SetDebugSearch(boolean withAspirationWindows, DebugNodeTrap debugNodeTrap, boolean showOnlyPV, boolean showNodeTranspositionAccess, boolean showNodeSorterTranspositionAccess) {
        this.withAspirationWindows = withAspirationWindows;
        this.debugNodeTrap = debugNodeTrap;
        this.showOnlyPV = showOnlyPV;
        this.showNodeTranspositionAccess = showNodeTranspositionAccess;
        this.showNodeSorterTranspositionAccess = showNodeSorterTranspositionAccess;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        try {
            fos = new FileOutputStream(String.format("DebugSearchTree-%s.txt", dtFormatter.format(Instant.now())));
            bos = new BufferedOutputStream(fos);
            debugOut = new PrintStream(bos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        debugOut.print("Search started\n");

        searchTracker = new SearchTracker();

        context.setSearchTracker(searchTracker);

        if (debugNodeTrap != null && debugNodeTrap instanceof SearchByCycleListener debugNodeTrapSearchByCycleListener) {
            debugNodeTrapSearchByCycleListener.beforeSearch(context);
        }
    }

    @Override
    public void afterSearch(SearchMoveResult searchMoveResult) {
        if (debugNodeTrap != null && debugNodeTrap instanceof SearchByCycleListener debugNodeTrapSearchByCycleListener) {
            debugNodeTrapSearchByCycleListener.afterSearch(searchMoveResult);
        }

        debugOut.print("Search completed\n");

        try {
            debugOut.flush();
            debugOut.close();
            bos.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        debugOut.printf("Search by depth %d started\n", context.getMaxPly());
        if (debugNodeTrap != null && debugNodeTrap instanceof SearchByDepthListener debugNodeTrapSearchByDepthListener) {
            debugNodeTrapSearchByDepthListener.beforeSearchByDepth(context);
        }
    }

    @Override
    public void afterSearchByDepth(SearchByDepthResult result) {
        if (debugNodeTrap != null && debugNodeTrap instanceof SearchByDepthListener debugNodeTrapSearchByDepthListener) {
            debugNodeTrapSearchByDepthListener.afterSearchByDepth(result);
        }
        if (!withAspirationWindows) {
            dumpSearchTracker();
        }
        debugOut.print("Search by depth completed\n");
        debugOut.printf("bestMove=%s; evaluation=%d; ", simpleMoveEncoder.encode(result.getBestMove()), result.getBestEvaluation());
        debugOut.printf("depth %d seldepth %d pv %s\n\n", result.getDepth(), result.getDepth(), simpleMoveEncoder.encodeMoves(result.getPrincipalVariation()));
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        debugOut.printf("WIN alpha=%d beta=%d cycle=%d\n", alphaBound, betaBound, searchByWindowsCycle);
    }

    @Override
    public void afterSearchByWindows(boolean searchByWindowsFinished) {
        dumpSearchTracker();
    }

    private void dumpSearchTracker() {
        debugErrorMessages = new LinkedList<>();
        dumpNode(searchTracker.getRootNode());
        debugErrorMessages.forEach(debugOut::println);
        debugOut.flush();
        searchTracker.reset();
    }

    private void dumpNode(DebugNode currentNode) {
        dumpNodeHeader(currentNode);


        if (currentNode.sortedMovesStr != null) {
            debugOut.printf("%s Exploring: %s\n", ">\t".repeat(currentNode.ply), currentNode.sortedMovesStr);
        }

        if (showNodeSorterTranspositionAccess) {
            if (!currentNode.sorterReads.isEmpty()) {
                debugOut.printf("%s Sorter Reads:\n", ">\t".repeat(currentNode.ply));
                for (DebugNodeTT ttOperation :
                        currentNode.sorterReads) {

                    int ttValue = TranspositionEntry.decodeValue(ttOperation.getMovesAndValue());

                    debugOut.printf("%s ReadTT[ %s %s 0x%s depth=%d value=%d ]",
                            ">\t".repeat(currentNode.ply),
                            ttOperation.getTableName(),
                            ttOperation.getBound(),
                            hexFormat.formatHex(longToByte(ttOperation.getHash())),
                            ttOperation.getDepth(),
                            ttValue);

                    debugOut.print("\n");
                }
            }
        }

        if (debugNodeTrap != null) {
            if (debugNodeTrap.test(currentNode)) {
                debugNodeTrap.debug(currentNode, debugOut);
            }
        }


        for (DebugNode childNode : currentNode.childNodes) {
            if (showOnlyPV) {
                if (childNode.type.equals(DebugNode.NodeType.PV)) {
                    dumpNode(childNode);
                } else {
                    dumpNodeHeader(childNode);
                }
            } else {
                dumpNode(childNode);
            }
        }
    }

    private void dumpNodeHeader(DebugNode currentNode) {
        if (currentNode.ply > 0) {
            String moveStr = simpleMoveEncoder.encode(currentNode.selectedMove);
            debugOut.printf("%s%s ", ">\t".repeat(currentNode.ply), moveStr);
        }

        debugOut.printf("%s %s 0x%s alpha=%d beta=%d", currentNode.fnString, currentNode.topology, hexFormat.formatHex(longToByte(currentNode.zobristHash)), currentNode.alpha, currentNode.beta);

        if (currentNode.standingPat != null) {
            debugOut.printf(" SP=%d", currentNode.standingPat);
        }

        debugOut.printf(" value=%d %s", currentNode.value, currentNode.type);

        if (Objects.nonNull(currentNode.parent) &&
                currentNode.parent.childNodes.stream()
                        .filter(otherNode -> otherNode.zobristHash == currentNode.zobristHash)
                        .count() > 1) {
            debugOut.print(" DUPLICATED CHILD NODE");
            debugErrorMessages.add(String.format("DUPLICATED CHILD NODE %s", currentNode.zobristHash));
        }

        debugOut.print("\n");

        if (showNodeTranspositionAccess) {
            if (currentNode.entryRead != null) {
                int ttValue = TranspositionEntry.decodeValue(currentNode.entryRead.getMovesAndValue());
                debugOut.printf("%s ReadTT[ %s %s depth=%d value=%d ]",
                        ">\t".repeat(currentNode.ply),
                        currentNode.entryRead.getTableName(),
                        currentNode.entryRead.getBound(),
                        currentNode.entryRead.getDepth(),
                        ttValue);
                if (currentNode.zobristHash != currentNode.entryRead.getHash()) {
                    debugOut.print(" WRONG TT_READ ENTRY");
                    debugErrorMessages.add(String.format("WRONG TT_READ ENTRY %s", currentNode.zobristHash));
                }
                debugOut.print("\n");
            }

            if (currentNode.entryWrite != null) {
                int ttValue = TranspositionEntry.decodeValue(currentNode.entryWrite.getMovesAndValue());
                debugOut.printf("%s WriteTT[ %s %s depth=%d value=%d ]",
                        ">\t".repeat(currentNode.ply),
                        currentNode.entryWrite.getTableName(),
                        currentNode.entryWrite.getBound(),
                        currentNode.entryWrite.getDepth(),
                        ttValue);

                if (currentNode.zobristHash != currentNode.entryWrite.getHash_requested()) {
                    debugOut.print(" WRONG TT_WRITE_HASH_REQUESTED");
                    debugErrorMessages.add(String.format("WRONG TT_WRITE_HASH_REQUESTED %s", currentNode.zobristHash));
                }

                if (currentNode.value != ttValue) {
                    debugOut.print(" WRONG TT_WRITE_VALUE");
                    debugErrorMessages.add(String.format("WRONG TT_WRITE_VALUE %s", currentNode.zobristHash));
                }
                debugOut.print("\n");
            }
        }
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
