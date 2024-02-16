package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import net.chesstango.board.moves.Move;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
@Getter
public class DebugNode {

    public enum NodeTopology {ROOT, INTERIOR, TERMINAL, HORIZON, LOOP, QUIESCENCE, CHECK_EXTENSION, LEAF}

    /**
     * PV-Node Knuth's Type 1
     * Cut-Node Knuth's Type 2, also called fail-high node
     * All-Node Knuth's Type 3, also called fail-low node
     */
    public enum NodeType {PV, CUT, ALL}

    NodeTopology topology;

    NodeType type;

    int ply;

    long zobristHash;

    DebugNode parent;

    Move selectedMove;

    String fnString;

    int alpha;

    int beta;

    int value;

    Integer standingPat;

    DebugOperationTT entryRead;

    DebugOperationTT entryWrite;

    List<String> sortedMoves;
    List<DebugOperationTT> sorterReads = new ArrayList<>();

    List<DebugNode> childNodes = new LinkedList<>();

    public void setZobristHash(long zobristHash) {
        this.zobristHash = zobristHash;
        if (Objects.nonNull(this.parent) &&
                this.parent.childNodes.stream()
                        .filter(otherNode -> otherNode.zobristHash == zobristHash)
                        .count() > 1) {
            throw new RuntimeException("Duplicated Node");
        }
    }

    public void setDebugSearch(String fnString, int alpha, int beta) {
        this.fnString = fnString;
        this.alpha = alpha;
        this.beta = beta;
    }
}
