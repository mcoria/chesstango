package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Mauricio Coria
 */
public class TTDump implements SearchLifeCycle {
    private Game game;
    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;

    private boolean initialStateDumped = false;

    @Override
    public void init(SearchContext context) {
        this.game = context.getGame();
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
                if(tableEntry.type != null) {
                    dos.writeByte(tableEntry.type.toByte());
                } else {
                    dos.writeByte(0);
                }

                dos.writeLong(tableEntry.qBestMoveAndValue);
                dos.writeInt(tableEntry.qValue);
                if(tableEntry.qType != null) {
                    dos.writeByte(tableEntry.qType.toByte());
                } else {
                    dos.writeByte(0);
                }

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
