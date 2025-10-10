package net.chesstango.search.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Mauricio Coria
 */
public class SetZobristMemoryVisitor implements Visitor {
    private final Map<Long, String> zobristMaxMap;
    private final Map<Long, String> zobristMinMap;
    private final List<String> zobristCollisions;

    public SetZobristMemoryVisitor(Map<Long, String> zobristMaxMap, Map<Long, String> zobristMinMap, List<String> zobristCollisions) {
        this.zobristMaxMap = zobristMaxMap;
        this.zobristMinMap = zobristMinMap;
        this.zobristCollisions = zobristCollisions;
    }

    @Override
    public void visit(ZobristTracker zobristTracker) {
        zobristTracker.setZobristMaxMap(zobristMaxMap);
        zobristTracker.setZobristMinMap(zobristMinMap);
        zobristTracker.setZobristCollisions(zobristCollisions);
    }
}
