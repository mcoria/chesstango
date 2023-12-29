package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.debug.SearchNode;
import net.chesstango.search.smart.debug.SearchNodeTT;
import net.chesstango.search.smart.debug.SearchTracker;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetDebugSearchTree implements SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener {
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault());
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();
    private HexFormat hexFormat = HexFormat.of().withUpperCase();
    private final boolean withAspirationWindows;
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private PrintStream debugOut;
    private SearchTracker searchTracker;

    public SetDebugSearchTree(boolean withAspirationWindows) {
        this.withAspirationWindows = withAspirationWindows;
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
    }

    @Override
    public void afterSearch() {
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
    public void afterSearchByDepth(SearchMoveResult result) {
        if (!withAspirationWindows) {
            dumpSearchTracker();
        }

        debugOut.print("Search by depth completed\n");
        debugOut.printf("bestMove=%s; evaluation=%d; ", simpleMoveEncoder.encode(result.getBestMove()), result.getEvaluation());
        debugOut.printf("depth %d seldepth %d pv %s\n\n", result.getDepth(), result.getDepth(), "-");
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        debugOut.printf("WIN alpha=%d beta=%d cycle=%d\n", alphaBound, betaBound, searchByWindowsCycle);
    }

    @Override
    public void afterSearchByWindows(boolean searchByWindowsFinished) {
        if(searchByWindowsFinished) {
            dumpSearchTracker();
        }else {
            searchTracker.reset();
        }
    }


    private void dumpSearchTracker() {
        dumpNode(0, searchTracker.getRootNode());
        searchTracker.reset();
    }

    private void dumpNode(int depth, SearchNode currentNode) {
        if (depth == 0) {
            debugOut.printf("%s alpha=%d beta=%d", currentNode.getFnString(), currentNode.getAlpha(), currentNode.getBeta());
        } else {
            String moveStr = simpleMoveEncoder.encode(currentNode.getSelectedMove());

            debugOut.printf("%s%s %s alpha=%d beta=%d", ">\t".repeat(depth), moveStr, currentNode.getFnString(), currentNode.getAlpha(), currentNode.getBeta());
        }

        if (currentNode.getStandingPat() != null) {
            debugOut.printf(" SP=%d", currentNode.getStandingPat());
        }

        debugOut.printf(" hash=0x%s", hexFormat.formatHex(longToByte(currentNode.getZobristHash())));

        debugOut.printf(" value=%d", currentNode.getValue());

        debugOut.print("\n");

        for (SearchNodeTT ttOperations :
                currentNode.getTranspositionOperations()) {

            if (SearchNodeTT.TranspositionOperationType.READ.equals(ttOperations.transpositionOperation())) {
                int ttValue = TranspositionEntry.decodeValue(ttOperations.movesAndValue());
                debugOut.printf("%s ReadTT[ %s 0x%s depth=%d value=%d]\n",
                        ">\t".repeat(depth),
                        ttOperations.tableName(),
                        hexFormat.formatHex(longToByte(ttOperations.hash())),
                        ttOperations.depth(),
                        ttValue);
            }

            if (SearchNodeTT.TranspositionOperationType.WRITE.equals(ttOperations.transpositionOperation())) {
                int ttValue = TranspositionEntry.decodeValue(ttOperations.movesAndValue());

                if (currentNode.getValue() != ttValue) {
                    throw new RuntimeException("currentNodeTracker.value != ttValue");
                }

                debugOut.printf("%s WriteTT[ %s 0x%s depth=%d value=%d]\n",
                        ">\t".repeat(depth),
                        ttOperations.tableName(),
                        hexFormat.formatHex(longToByte(ttOperations.hash())),
                        ttOperations.depth(),
                        ttValue);
            }
        }

        //int nextDepth = SearchNode.SearchNodeType.HORIZON.equals(currentNode.getNodeType()) ? depth : depth + 1;

        for (SearchNode childNode : currentNode.getChildNodes()) {
            dumpNode(depth + 1, childNode);
        }
    }


    private String getPrincipalVariation(SearchMoveResult result) {
        StringBuilder sb = new StringBuilder();
        List<Move> pv = result.getPrincipalVariation();
        for (Move move : pv) {
            sb.append(simpleMoveEncoder.encode(move));
            sb.append(" ");
        }
        return sb.toString();
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
