package net.chesstango.search.smart.alphabeta.transposition.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Mauricio Coria
 */
@Setter
public class TTLoad implements SearchByDepthListener, Acceptor {

    private Game game;
    private TTable maxMap;
    private TTable minMap;

    private boolean initialStateLoaded = false;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearchByDepth() {
        if ("8/p7/2R5/4k3/8/Pp1b3P/1r3PP1/6K1 w - - 2 43".equals(game.toString()) && !initialStateLoaded) {
            loadTables();
            initialStateLoaded = true;
        }
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

    private void loadTable(String fileName, TTable map) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);

            while (dis.available() > 0) {
                long key = dis.readLong();
                TranspositionEntry tableEntry = new TranspositionEntry();
                tableEntry.setDraft(dis.readInt());
                tableEntry.setMove(dis.readShort());
                tableEntry.setValue(dis.readInt());
                //tableEntry.value = dis.readInt();
                //tableEntry.transpositionType = TranspositionType.valueOf(dis.readByte());

                //tableEntry.qBestMoveAndValue = dis.readLong();
                //tableEntry.qValue = dis.readInt();
                //tableEntry.qTranspositionType = TranspositionType.valueOf(dis.readByte());

                //map.put(key, tableEntry);
            }

            dis.close();
            bis.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            throw new RuntimeException(ioe);
        }
    }

}
