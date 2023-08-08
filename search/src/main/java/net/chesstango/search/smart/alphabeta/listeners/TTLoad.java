package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static net.chesstango.search.smart.Transposition.Type;

import net.chesstango.search.smart.Transposition;

/**
 * @author Mauricio Coria
 */
public class TTLoad implements SearchLifeCycle {

    private Game game;
    private Map<Long, Transposition> maxMap;
    private Map<Long, Transposition> minMap;

    private boolean initialStateLoaded = false;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();

        if ("8/p7/2R5/4k3/8/Pp1b3P/1r3PP1/6K1 w - - 2 43".equals(game.toString()) && !initialStateLoaded) {
            loadTables();
            initialStateLoaded = true;
        }
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    private void loadTables() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        System.out.println("Loading ...");
        Future<?> task1 = executorService.submit(() -> loadTable("C:\\Java\\projects\\chess\\chesstango\\maxMap-0.ser", maxMap));
        Future<?> task2 = executorService.submit(() -> loadTable("C:\\Java\\projects\\chess\\chesstango\\minMap-0.ser", minMap));

        while (!(task1.isDone() && task2.isDone())) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Loading finished");

        executorService.shutdown();
    }

    private void loadTable(String fileName, Map<Long, Transposition> map) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);

            while (dis.available() > 0) {
                long key = dis.readLong();
                Transposition tableEntry = new Transposition();
                tableEntry.searchDepth = dis.readInt();
                tableEntry.bestMoveAndValue = dis.readLong();
                tableEntry.value = dis.readInt();
                tableEntry.type = Type.valueOf(dis.readByte());

                tableEntry.qBestMoveAndValue = dis.readLong();
                tableEntry.qValue = dis.readInt();
                tableEntry.qType = Type.valueOf(dis.readByte());

                map.put(key, tableEntry);
            }

            dis.close();
            bis.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException(ioe);
        }
    }

}
