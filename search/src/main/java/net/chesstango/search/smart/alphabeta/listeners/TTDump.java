package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static net.chesstango.search.smart.SearchContext.EntryType;

/**
 * @author Mauricio Coria
 */
public class TTDump implements SearchLifeCycle {
    private Game game;
    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;

    private boolean initialStateDumped = false;

    @Override
    public void initSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void closeSearch(SearchMoveResult result) {

    }

    @Override
    public void init(SearchContext context) {
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();

        if ("8/p7/2R5/4k3/8/Pp1b3P/1r3PP1/6K1 w - - 2 43".equals(game.toString()) && !initialStateDumped) {
            dumpTables(0);
            initialStateDumped = true;
        }
    }

    @Override
    public void close(SearchMoveResult result) {
        if ("8/p7/2R5/4k3/8/Pp1b3P/1r3PP1/6K1 w - - 2 43".equals(game.toString())) {
            dumpTables(result.getDepth());
        }
    }

    @Override
    public void reset() {

    }

    private void dumpTables(int searchCycle) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        System.out.println("Dumping " + searchCycle);
        Future<?> task1 = executorService.submit(() -> dumpTable(String.format("%s-%d.ser", "maxMap", searchCycle), maxMap));
        Future<?> task2 = executorService.submit(() -> dumpTable(String.format("%s-%d.ser", "minMap", searchCycle), minMap));

        while(! ( task1.isDone() && task2.isDone() ) ){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        executorService.shutdown();
    }

    private void dumpTable(String fileName, Map<Long, SearchContext.TableEntry> map) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);

            int counter = 0;

            Set<Map.Entry<Long, SearchContext.TableEntry>> entries = map.entrySet();
            for (Map.Entry<Long, SearchContext.TableEntry> entry: entries) {
                dos.writeLong(entry.getKey());

                SearchContext.TableEntry tableEntry = entry.getValue();
                dos.writeInt(tableEntry.searchDepth);
                dos.writeLong(tableEntry.bestMoveAndValue);
                dos.writeInt(tableEntry.value);
                dos.writeByte(EntryType.encode(tableEntry.type));

                dos.writeLong(tableEntry.qBestMoveAndValue);
                dos.writeInt(tableEntry.qValue);
                dos.writeByte(EntryType.encode(tableEntry.qType));

                counter++;
            }
            dos.flush();
            bos.flush();
            fos.flush();

            dos.close();
            bos.close();
            fos.close();

            System.out.println(String.format("Done file %s, entries %d", fileName, counter));
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            throw new RuntimeException(ioe);
        } catch (Exception e){
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

}
