package net.chesstango.search.smart.features.debug.listeners;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.*;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByWindowsListener;
import net.chesstango.search.smart.features.debug.DebugNodeTrap;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.debug.model.DebugOperationEval;
import net.chesstango.search.smart.features.debug.model.DebugOperationTT;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

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
public class SetDebugOutput implements SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener, Acceptor {
    private final boolean showOnlyPV;
    private final boolean showNodeTranspositionAccess;
    private final boolean showSorterOperations;
    private final boolean withAspirationWindows;
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault());
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();
    private final HexFormat hexFormat = HexFormat.of().withUpperCase();
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private PrintStream debugOut;

    @Setter
    private int depth;

    @Setter
    private SearchTracker searchTracker;

    @Setter
    private DebugNodeTrap debugNodeTrap;

    private List<String> debugErrorMessages;

    public SetDebugOutput(boolean withAspirationWindows, boolean showOnlyPV, boolean showNodeTranspositionAccess, boolean showSorterOperations) {
        this.withAspirationWindows = withAspirationWindows;
        this.showOnlyPV = showOnlyPV;
        this.showNodeTranspositionAccess = showNodeTranspositionAccess;
        this.showSorterOperations = showSorterOperations;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
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
    public void afterSearch(SearchResult result) {
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
    public void beforeSearchByDepth() {
        debugOut.printf("Search by depth %d started\n", depth);
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth result) {
        if (!withAspirationWindows) {
            dumpSearchTracker();
        }
        debugOut.print("Search by depth completed\n");
        debugOut.printf("bestMove=%s; evaluation=%d; ", simpleMoveEncoder.encode(result.getBestMove()), result.getBestEvaluation());
        debugOut.printf("depth %d seldepth %d pv %s\n\n", result.getDepth(), result.getDepth(), simpleMoveEncoder.encode(result.getPrincipalVariation().stream().map(PrincipalVariation::move).toList()));
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

        if (showNodeTranspositionAccess) {
            showNodeTranspositionAccess(currentNode);
        }

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

        if (showNodeTranspositionAccess) {
            showNodePVTranspositionAccess(currentNode);
        }
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
    }

    private void showNodeTranspositionAccess(DebugNode currentNode) {
        currentNode.getEntryRead().forEach(readOp -> {
            TranspositionEntry entry = readOp.getEntry();
            int ttValue = entry.getValue();
            debugOut.printf("%s ReadTT[ %s 0x%s %s draft=%d move=%s value=%d ]",
                    ">\t".repeat(currentNode.getPly()),
                    readOp.getTableType(),
                    hexFormat.formatHex(longToByte(entry.hash)),
                    entry.getTranspositionBound(),
                    entry.getDraft(),
                    readOp.getMove(),
                    ttValue);
            if (currentNode.getZobristHash() != entry.getHash()) {
                debugOut.print(" WRONG TT_READ ENTRY");
                debugErrorMessages.add(String.format("WRONG TT_READ ENTRY 0x%s", hexFormat.formatHex(longToByte(currentNode.getZobristHash()))));
            }
            debugOut.print("\n");
        });

        currentNode.getEntryWrite().forEach(writeOp -> {
            TranspositionEntry entry = writeOp.getEntry();
            int ttValue = entry.getValue();
            debugOut.printf("%s WriteTT[ %s 0x%s %s draft=%d move=%s value=%d ]",
                    ">\t".repeat(currentNode.getPly()),
                    writeOp.getTableType(),
                    hexFormat.formatHex(longToByte(entry.hash)),
                    entry.getTranspositionBound(),
                    entry.getDraft(),
                    writeOp.getMove(),
                    ttValue);

            if (currentNode.getZobristHash() != entry.getHash()) {
                String message = String.format(" WRONG TT_WRITE_HASH_REQUESTED ENTRY 0x%s", hexFormat.formatHex(longToByte(currentNode.getZobristHash())));
                debugOut.print(message);
                debugErrorMessages.add(message);
            }

            if (currentNode.getValue() != ttValue) {
                String message = String.format(" WRONG TT_WRITE_VALUE %s", currentNode.getValue());
                debugOut.print(message);
                debugErrorMessages.add(message);
            }
            debugOut.print("\n");
        });
    }

    private void dumpSorterOperations(DebugNode currentNode) {
        List<String> sortedMoves = currentNode.getSortedMoves();

        List<DebugOperationTT> sortedReads = currentNode.getSorterReads();

        List<DebugOperationEval> evalCacheReads = currentNode.getEvalCacheReads();

        List<Move> sorterKms = currentNode.getSorterKm();

        debugOut.printf("%s Sorter transpositions=%d cache=%d ply=%d\n", ">\t".repeat(currentNode.getPly()), sortedReads.size(), evalCacheReads.size(), currentNode.getSortedPly());

        sortedMoves.forEach(moveStr -> {
            sortedReads
                    .stream()
                    .filter(debugNodeTT -> Objects.equals(moveStr, debugNodeTT.getMove()))
                    .forEach(ttOperation ->
                    {
                        TranspositionEntry entry = ttOperation.getEntry();
                        debugOut.printf("%s Sorter %s ReadTT[ %s %s 0x%s draft=%d move=? value=%d ]\n",
                                ">\t".repeat(currentNode.getPly()),
                                moveStr,
                                ttOperation.getTableType(),
                                entry.getTranspositionBound(),
                                hexFormat.formatHex(longToByte(entry.getHash())),
                                entry.getDraft(),
                                entry.getValue());
                    });


            evalCacheReads.stream()
                    .filter(debugOperationEval -> Objects.equals(moveStr, debugOperationEval.getMove()))
                    .forEach(debugOperationEval ->
                    {
                        debugOut.printf("%s Sorter %s CacheRead[ 0x%s value=%d ]\n",
                                ">\t".repeat(currentNode.getPly()),
                                moveStr,
                                hexFormat.formatHex(longToByte(debugOperationEval.getHashRequested())),
                                debugOperationEval.getEvaluation());
                    });

            sorterKms.stream()
                    .map(simpleMoveEncoder::encode)
                    .filter(kmStr -> Objects.equals(kmStr, moveStr))
                    .forEach(kmStr ->
                    {
                        debugOut.printf("%s Sorter %s KillerMove\n",
                                ">\t".repeat(currentNode.getPly()),
                                moveStr);
                    });
        });


        /**
         * NO ME QUEDA CLARO PARA QUE MOVIMIENTO LEEMOS EN ESTE CASO
         */
        sortedReads
                .stream()
                .filter(ttOperation -> "NO_MOVE".equals(ttOperation.getMove()))
                .forEach(ttOperation -> {
                    TranspositionEntry entry = ttOperation.getEntry();
                    int ttValue = entry.getValue();
                    debugOut.printf("%s Sorter NO_MOVE ReadTT[ %s %s 0x%s draft=%d move=? value=%d ]",
                            ">\t".repeat(currentNode.getPly()),
                            ttOperation.getTableType(),
                            entry.getTranspositionBound(),
                            hexFormat.formatHex(longToByte(entry.getHash())),
                            entry.getDraft(),
                            ttValue);
                    debugOut.print("\n");
                });

    }

    private void showNodePVTranspositionAccess(DebugNode currentNode) {
        currentNode.getPvReads().forEach(readOp -> {
            TranspositionEntry entry = readOp.getEntry();
            debugOut.printf(" PV ReadTT[ %s 0x%s %s draft=%d move=? value=%d ]\n",
                    readOp.getTableType(),
                    hexFormat.formatHex(longToByte(entry.hash)),
                    entry.getTranspositionBound(),
                    entry.getDraft(),
                    entry.getValue());
        });
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
