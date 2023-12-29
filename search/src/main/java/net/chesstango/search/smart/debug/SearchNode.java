package net.chesstango.search.smart.debug;

import lombok.Getter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.transposition.TranspositionBound;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SearchNode {
    @Getter
    SearchTracker.NodeType nodeType;
    @Getter
    long zobristHash;
    @Getter
    SearchNode parent;
    @Getter
    Move selectedMove;
    @Getter
    String fnString;
    @Getter
    int alpha;
    @Getter
    int beta;
    @Getter
    int value;
    @Getter
    Integer standingPat;

    @Getter
    List<SearchNode> childNodes = new LinkedList<>();

    @Getter
    List<SearchNodeTT> transpositionOperations = new LinkedList<>();

    public void addChild(SearchNode newNode) {
        childNodes.add(newNode);
        newNode.parent = this;
    }
}
