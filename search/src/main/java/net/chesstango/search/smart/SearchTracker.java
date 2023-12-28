package net.chesstango.search.smart;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SearchTracker {

    public enum NodeType {ROOT, INTERIOR, TERMINAL, HORIZON, QUIESCENCE, Q_LEAF}

    private SearchNodeTracker currentNodeTracker;

    public void newNode(NodeType nodeType) {
        SearchNodeTracker newNode = new SearchNodeTracker();
        newNode.nodeType = nodeType;

        if (currentNodeTracker != null) {
            currentNodeTracker.addChild(newNode);
        }

        currentNodeTracker = newNode;
    }

    public void setDebugSearch(String fnString, int alpha, int beta) {
        currentNodeTracker.fnString = fnString;
        currentNodeTracker.alpha = alpha;
        currentNodeTracker.beta = beta;
    }

    public void setSelectedMove(Move currentMove) {
        currentNodeTracker.selectedMove = currentMove;
    }

    public void setValue(int value) {
        currentNodeTracker.value = value;
    }

    public void setStandingPat(Integer value) {
        currentNodeTracker.standingPat = value;
    }

    public void setZobristHash(long zobristHash) {
        currentNodeTracker.zobristHash = zobristHash;
    }

    public void trackReadTranspositionEntry(String tableName, TranspositionEntry entry) {
        if (entry != null) {
            currentNodeTracker.readTT = true;
            currentNodeTracker.read_tableName = tableName;
            currentNodeTracker.read_hash = entry.hash;
            currentNodeTracker.read_searchDepth = entry.searchDepth;
            currentNodeTracker.read_movesAndValue = entry.movesAndValue;
            currentNodeTracker.read_bound = entry.transpositionBound;
        }
    }

    public void trackWriteTranspositionEntry(String tableName, long hash, int searchDepth, long movesAndValue, TranspositionBound bound) {
        currentNodeTracker.writeTT = true;
        currentNodeTracker.write_tableName = tableName;
        currentNodeTracker.write_hash = hash;
        currentNodeTracker.write_searchDepth = searchDepth;
        currentNodeTracker.write_movesAndValue = movesAndValue;
        currentNodeTracker.write_bound = bound;
    }

    public void save() {
        if (currentNodeTracker.parent != null) {
            currentNodeTracker = currentNodeTracker.parent;
        }
    }

    public void reset() {
        currentNodeTracker = null;
    }

    public SearchNodeTracker getRootNode() {
        if (currentNodeTracker.parent != null) {
            throw new RuntimeException("Still searching?");
        }
        return currentNodeTracker;
    }


    public class SearchNodeTracker {
        public NodeType nodeType;
        public long zobristHash;
        public SearchNodeTracker parent;
        public Move selectedMove;
        public String fnString;
        public int alpha;
        public int beta;
        public int value;
        public Integer standingPat;

        public boolean readTT;
        public String read_tableName;
        public long read_hash;
        public int read_searchDepth;
        public long read_movesAndValue;
        public TranspositionBound read_bound;

        public boolean writeTT;
        public String write_tableName;
        public long write_hash;
        public int write_searchDepth;
        public long write_movesAndValue;
        public TranspositionBound write_bound;

        public List<SearchNodeTracker> childNodes = new LinkedList<>();

        public void addChild(SearchNodeTracker newNode) {
            childNodes.add(newNode);
            newNode.parent = this;
        }
    }
}
