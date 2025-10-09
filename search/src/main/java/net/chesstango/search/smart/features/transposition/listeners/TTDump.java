package net.chesstango.search.smart.features.transposition.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Mauricio Coria
 */
@Setter
public class TTDump implements SearchByDepthListener, Acceptor {
    private Game game;
    private TTable maxMap;
    private TTable minMap;

    private boolean initialStateDumped = false;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearchByDepth() {
        if ("8/p7/2R5/4k3/8/Pp1b3P/1r3PP1/6K1 w - - 2 43".equals(game.toString()) && !initialStateDumped) {
            dumpTables(0);
            initialStateDumped = true;
        }
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth result) {
        if ("8/p7/2R5/4k3/8/Pp1b3P/1r3PP1/6K1 w - - 2 43".equals(game.toString())) {
            dumpTables(result.getDepth());
        }
    }

    private void dumpTables(int searchCycle) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        System.out.println("Dumping " + searchCycle);
        Future<?> task1 = executorService.submit(() -> dumpTable(String.format("%s-%d.ser", "maxMap", searchCycle), maxMap));
        Future<?> task2 = executorService.submit(() -> dumpTable(String.format("%s-%d.ser", "minMap", searchCycle), minMap));

        while (!(task1.isDone() && task2.isDone())) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        executorService.shutdown();
    }

    private void dumpTable(String fileName, TTable map) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);

            int counter = 0;

            Set<Map.Entry<Long, TranspositionEntry>> entries = null; //map.entrySet();
            for (Map.Entry<Long, TranspositionEntry> entry : entries) {
                dos.writeLong(entry.getKey());

                TranspositionEntry tableEntry = entry.getValue();
                dos.writeInt(tableEntry.searchDepth);
                dos.writeLong(tableEntry.movesAndValue);
                //dos.writeInt(tableEntry.value);
                //dos.writeByte(TranspositionType.encode(tableEntry.transpositionType));

                //dos.writeLong(tableEntry.qBestMoveAndValue);
                //dos.writeInt(tableEntry.qValue);
                //dos.writeByte(TranspositionType.encode(tableEntry.qTranspositionType));

                counter++;
            }
            dos.flush();
            bos.flush();
            fos.flush();

            dos.close();
            bos.close();
            fos.close();

            System.out.printf("Done file %s, entries %d%n", fileName, counter);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

}
