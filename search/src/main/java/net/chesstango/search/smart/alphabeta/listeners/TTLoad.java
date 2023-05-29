package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class TTLoad implements SearchLifeCycle {

    private Game game;
    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;

    private boolean initialStateLoaded = false;

    @Override
    public void init(SearchContext context) {
        this.game = context.getGame();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();

        if ("8/p7/2R5/4k3/8/Pp1b3P/1r3PP1/6K1 w - - 2 43".equals(game.toString()) && !initialStateLoaded) {
            loadTables();
            initialStateLoaded = true;
        }
    }

    @Override
    public void close(SearchMoveResult result) {
    }

    private void loadTables() {
        loadTable("C:\\Java\\projects\\chess\\chesstango\\maxMap-0.ser", maxMap);
        loadTable("C:\\Java\\projects\\chess\\chesstango\\minMap-0.ser", minMap);
    }

    private void loadTable(String fileName, Map<Long, SearchContext.TableEntry> map) {
        Map<Long, SearchContext.TableEntry> mapFromDisk = readTable(fileName);

        Set set = mapFromDisk.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry<Long, SearchContext.TableEntry> mentry = (Map.Entry) iterator.next();
            map.put(mentry.getKey(), mentry.getValue());
        }
    }

    private Map<Long, SearchContext.TableEntry> readTable(String fileName) {
        Map<Long, SearchContext.TableEntry> map = null;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException(ioe);
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            throw new RuntimeException(c);
        }
        return map;
    }
}
