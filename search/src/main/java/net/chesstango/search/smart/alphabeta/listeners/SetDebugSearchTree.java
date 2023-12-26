package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetDebugSearchTree implements SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener {
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault());
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();
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
        dumpSearchTracker();
    }


    private void dumpSearchTracker() {
        dumpNode(0, searchTracker.getRootNode());
        searchTracker.reset();
    }

    private void dumpNode(int depth, SearchTracker.SearchNodeTracker currentNode) {
        if (depth == 0) {
            debugOut.printf("%s alpha=%d beta=%d value=%d\n", currentNode.fnString, currentNode.alpha, currentNode.beta, currentNode.value);
        } else {
            String moveStr = currentNode.selectedMove == null ? "" : simpleMoveEncoder.encode(currentNode.selectedMove);

            debugOut.printf("%s%s %s alpha=%d beta=%d value=%d", ">\t".repeat(depth), moveStr, currentNode.fnString, currentNode.alpha, currentNode.beta, currentNode.value);

            if (currentNode.standingPat != null) {
                debugOut.printf(" SP=%d", currentNode.standingPat);
            }

            debugOut.print("\n");
        }

        for (SearchTracker.SearchNodeTracker childNode : currentNode.childNodes) {
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
}
