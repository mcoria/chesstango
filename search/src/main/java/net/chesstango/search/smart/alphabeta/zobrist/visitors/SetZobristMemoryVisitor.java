package net.chesstango.search.smart.alphabeta.zobrist.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Mauricio Coria
 */
public class SetZobristMemoryVisitor implements Visitor {
    private final Map<Long, String> zobristMaxMap;
    private final List<String> zobristCollisions;

    public SetZobristMemoryVisitor(Map<Long, String> zobristMaxMap, List<String> zobristCollisions) {
        this.zobristMaxMap = zobristMaxMap;
        this.zobristCollisions = zobristCollisions;
    }

    @Override
    public void visit(ZobristTracker zobristTracker) {
        zobristTracker.setZobristMap(zobristMaxMap);
        zobristTracker.setZobristCollisions(zobristCollisions);
    }
}
