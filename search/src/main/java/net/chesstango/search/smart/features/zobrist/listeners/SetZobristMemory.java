package net.chesstango.search.smart.features.zobrist.listeners;

import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.ResetListener;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class SetZobristMemory implements SearchByCycleListener, ResetListener {

    private final Map<Long, String> zobristMaxMap = new HashMap<>();
    private final Map<Long, String> zobristMinMap = new HashMap<>();
    private final List<String> zobristCollisions = new LinkedList<>();


    @Override
    public void beforeSearch(SearchByCycleContext context) {
        context.setZobristMaxMap(zobristMaxMap);
        context.setZobristMinMap(zobristMinMap);
        context.setZobristCollisions(zobristCollisions);
    }

    @Override
    public void afterSearch(SearchResult result) {
        if (zobristCollisions.isEmpty()) {
            System.out.println("No Zobrist collision");
        } else {
            System.out.println("Zobrist collisions:");
            zobristCollisions.forEach(collision -> System.out.printf("%s\n", collision));
        }
    }

    @Override
    public void reset() {
        zobristMaxMap.clear();
        zobristMinMap.clear();
        zobristCollisions.clear();
    }
}
