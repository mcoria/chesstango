package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationEval;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Mauricio Coria
 */
public class SetDebugOutput implements SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener {
    private final boolean showOnlyPV;
    private final boolean showNodeTranspositionAccess;
    private final boolean showSorterOperations;
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

    public SetDebugOutput(boolean withAspirationWindows, DebugNodeTrap debugNodeTrap, boolean showOnlyPV, boolean showNodeTranspositionAccess, boolean showSorterOperations) {
        this.withAspirationWindows = withAspirationWindows;
        this.debugNodeTrap = debugNodeTrap;
        this.showOnlyPV = showOnlyPV;
        this.showNodeTranspositionAccess = showNodeTranspositionAccess;
        this.showSorterOperations = showSorterOperations;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        searchTracker = context.getSearchTracker();

        try {
            fos = new FileOutputStream(String.format("DebugSearchTree-%s.txt", dtFormatter.format(Instant.now())));
            bos = new BufferedOutputStream(fos);
            debugOut = new PrintStream(bos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        debugOut.print("Search started\n");
    }

    @Override
    public void afterSearch(SearchMoveResult searchMoveResult) {
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
    }

    @Override
    public void afterSearchByDepth(SearchByDepthResult result) {
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
    }

    private void dumpNode(DebugNode currentNode) {
        dumpNodeHeader(currentNode);

        if (currentNode.getSortedMoves() != null) {
            debugOut.printf("%s Exploring: %s\n", ">\t".repeat(currentNode.getPly()), currentNode.getSortedMoves());

            if (showSorterOperations) {
                dumpSorterOperations(currentNode);
            }
        }

        if (debugNodeTrap != null && debugNodeTrap.test(currentNode)) {
            debugNodeTrap.debugAction(currentNode, debugOut);
        }

        for (DebugNode childNode : currentNode.getChildNodes()) {
            if (showOnlyPV) {
                if (childNode.getType().equals(DebugNode.NodeType.PV)) {
                    dumpNode(childNode);
                } else {
                    dumpNodeHeader(childNode);
                }
            } else {
                dumpNode(childNode);
            }
        }
    }

    private void dumpSorterOperations(DebugNode currentNode) {
        List<String> sortedMoves = currentNode.getSortedMoves();
        List<DebugOperationTT> sortedReads = currentNode.getSorterReads();
        List<DebugOperationEval> evalCacheReads = currentNode.getEvalCacheReads();

        debugOut.printf("%s Sorter Reads: transpositions=%d cache=%d\n", ">\t".repeat(currentNode.getPly()), sortedReads.size(), evalCacheReads.size());

        sortedMoves.forEach(moveStr -> {
            Optional<DebugOperationTT> ttOperationOpt = sortedReads
                    .stream()
                    .filter(debugNodeTT -> Objects.equals(moveStr, debugNodeTT.getMove()))
                    .findAny();


            if (ttOperationOpt.isPresent()) {
                DebugOperationTT ttOperation = ttOperationOpt.get();
                TranspositionEntry entry = ttOperation.getEntry();
                int ttValue = TranspositionEntry.decodeValue(entry.getMovesAndValue());
                debugOut.printf("%s  ReadTT[ %s %s 0x%s depth=%d value=%d ] %s",
                        ">\t".repeat(currentNode.getPly()),
                        ttOperation.getTableType(),
                        entry.getTranspositionBound(),
                        hexFormat.formatHex(longToByte(entry.getHash())),
                        entry.getSearchDepth(),
                        ttValue,
                        moveStr);
                debugOut.print("\n");
            }


            Optional<DebugOperationEval> evalOperationOpt = evalCacheReads.stream()
                    .filter(debugOperationEval -> Objects.equals(moveStr, debugOperationEval.getMove()))
                    .findAny();

            if (evalOperationOpt.isPresent()) {
                DebugOperationEval debugOperationEval = evalOperationOpt.get();
                debugOut.printf("%s  CacheRead[ 0x%s value=%d ] %s",
                        ">\t".repeat(currentNode.getPly()),
                        hexFormat.formatHex(longToByte(debugOperationEval.getHashRequested())),
                        debugOperationEval.getEvaluation(),
                        moveStr);
                debugOut.print("\n");
            }

        });


        sortedReads
                .stream()
                .filter(ttOperation -> "HORIZONTE".equals(ttOperation.getMove()))
                .forEach(ttOperation -> {
                    TranspositionEntry entry = ttOperation.getEntry();
                    int ttValue = TranspositionEntry.decodeValue(entry.getMovesAndValue());
                    debugOut.printf("%s ReadTT[ %s %s 0x%s depth=%d value=%d ] HORIZONTE",
                            ">\t".repeat(currentNode.getPly()),
                            ttOperation.getTableType(),
                            entry.getTranspositionBound(),
                            hexFormat.formatHex(longToByte(entry.getHash())),
                            entry.getSearchDepth(),
                            ttValue);
                    debugOut.print("\n");
                });
    }

    private void dumpNodeHeader(DebugNode currentNode) {
        if (currentNode.getPly() > 0) {
            String moveStr = simpleMoveEncoder.encode(currentNode.getSelectedMove());
            debugOut.printf("%s%s ", ">\t".repeat(currentNode.getPly()), moveStr);
        }

        debugOut.printf("%s %s 0x%s alpha=%d beta=%d", currentNode.getFnString(), currentNode.getTopology(), hexFormat.formatHex(longToByte(currentNode.getZobristHash())), currentNode.getAlpha(), currentNode.getBeta());

        if (currentNode.getStandingPat() != null) {
            debugOut.printf(" SP=%d", currentNode.getStandingPat());
        }

        debugOut.printf(" value=%d %s", currentNode.getValue(), currentNode.getType());

        if (Objects.nonNull(currentNode.getParent()) &&
                currentNode.getParent().getChildNodes().stream()
                        .filter(otherNode -> otherNode.getZobristHash() == currentNode.getZobristHash())
                        .count() > 1) {
            debugOut.print(" DUPLICATED CHILD NODE");
            debugErrorMessages.add(String.format("DUPLICATED CHILD NODE %s", currentNode.getZobristHash()));
        }

        debugOut.print("\n");

        if (showNodeTranspositionAccess) {
            currentNode.getEntryWrite().forEach(readOp -> {
                TranspositionEntry entry = readOp.getEntry();
                int ttValue = TranspositionEntry.decodeValue(entry.getMovesAndValue());
                debugOut.printf("%s ReadTT[ %s %s depth=%d value=%d ]",
                        ">\t".repeat(currentNode.getPly()),
                        readOp.getTableType(),
                        entry.getTranspositionBound(),
                        entry.getSearchDepth(),
                        ttValue);
                if (currentNode.getZobristHash() != entry.getHash()) {
                    debugOut.print(" WRONG TT_READ ENTRY");
                    debugErrorMessages.add(String.format("WRONG TT_READ ENTRY %s", currentNode.getZobristHash()));
                }
                debugOut.print("\n");
            });

            currentNode.getEntryWrite().forEach(writeOp -> {
                TranspositionEntry entry = writeOp.getEntry();
                int ttValue = TranspositionEntry.decodeValue(entry.getMovesAndValue());
                debugOut.printf("%s WriteTT[ %s %s depth=%d value=%d ]",
                        ">\t".repeat(currentNode.getPly()),
                        writeOp.getTableType(),
                        entry.getTranspositionBound(),
                        entry.getSearchDepth(),
                        ttValue);

                if (currentNode.getZobristHash() != writeOp.getHashRequested()) {
                    debugOut.print(" WRONG TT_WRITE_HASH_REQUESTED");
                    debugErrorMessages.add(String.format("WRONG TT_WRITE_HASH_REQUESTED %s", currentNode.getZobristHash()));
                }

                if (currentNode.getValue() != ttValue) {
                    debugOut.print(" WRONG TT_WRITE_VALUE");
                    debugErrorMessages.add(String.format("WRONG TT_WRITE_VALUE %s", currentNode.getZobristHash()));
                }
                debugOut.print("\n");
            });
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
