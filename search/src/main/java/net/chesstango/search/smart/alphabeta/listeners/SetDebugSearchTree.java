package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Color;
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
    private String searchType;
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault());

    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private PrintStream debugOut;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        try {
            fos = new FileOutputStream(String.format("DebugSearchTree-%s.txt", dtFormatter.format(Instant.now())));
            bos = new BufferedOutputStream(fos);
            debugOut = new PrintStream(bos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        context.setDebugOut(debugOut);

        debugOut.printf("Search started\n");
        searchType = Color.WHITE.equals(context.getGame().getChessPosition().getCurrentTurn()) ? "MAX" : "MIN";
    }

    @Override
    public void afterSearch() {
        debugOut.printf("Search completed\n");

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
        debugOut.printf(searchType);
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        StringBuilder sb = new StringBuilder();
        List<Move> pv = result.getPrincipalVariation();
        for (Move move : pv) {
            sb.append(simpleMoveEncoder.encode(move));
            sb.append(" ");
        }

        debugOut.print("\nSearch by depth completed\n");
        debugOut.printf("bestMove=%s; evaluation=%d; ", simpleMoveEncoder.encode(result.getBestMove()), result.getEvaluation());
        debugOut.printf("depth %d seldepth %d pv %s\n\n", result.getDepth(), result.getDepth(), sb);
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        debugOut.printf("\nWIN alpha=%d beta=%d cycle=%d", alphaBound, betaBound, searchByWindowsCycle);
    }

    @Override
    public void afterSearchByWindows(boolean searchByWindowsFinished) {
    }
}
