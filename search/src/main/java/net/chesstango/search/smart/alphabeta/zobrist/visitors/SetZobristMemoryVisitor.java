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
    private final Map<Long, String> zobristMap;
    private final List<String> zobristCollisions;

    public SetZobristMemoryVisitor(Map<Long, String> zobristMap, List<String> zobristCollisions) {
        this.zobristMap = zobristMap;
        this.zobristCollisions = zobristCollisions;
    }

    @Override
    public void visit(ZobristTracker zobristTracker) {
        zobristTracker.setZobristMap(zobristMap);
        zobristTracker.setZobristCollisions(zobristCollisions);
    }
}
