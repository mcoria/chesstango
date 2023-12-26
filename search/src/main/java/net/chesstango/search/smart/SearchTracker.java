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

    private SearchNodeTracker currentNodeTracker;

    public void newNode() {
        SearchNodeTracker newNode = new SearchNodeTracker();

        if (currentNodeTracker != null) {
            currentNodeTracker.addChild(newNode);
        }

        currentNodeTracker = newNode;
    }

    public void debugSearch(String fnString, int alpha, int beta) {
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


    public void writeTranspositionEntry(long hash, int searchDepth, long movesAndValue, TranspositionBound bound) {
    }

    public void readTranspositionEntry(TranspositionEntry entry) {
    }

    public void save() {
        if (currentNodeTracker.parent != null) {
            currentNodeTracker = currentNodeTracker.parent;
        }
    }

    public void reset() {
        currentNodeTracker = new SearchNodeTracker();
    }

    public SearchNodeTracker getRootNode() {
        if (currentNodeTracker.parent != null) {
            throw new RuntimeException("Still searching?");
        }
        return currentNodeTracker;
    }


    public class SearchNodeTracker {
        public SearchNodeTracker parent;
        public Move selectedMove;
        public String fnString;
        public int alpha;
        public int beta;
        public int value;
        public Integer standingPat;

        public List<SearchNodeTracker> childNodes = new LinkedList<>();

        public void addChild(SearchNodeTracker newNode) {
            childNodes.add(newNode);
            newNode.parent = this;
        }
    }
}
