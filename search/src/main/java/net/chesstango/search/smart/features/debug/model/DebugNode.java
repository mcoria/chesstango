package net.chesstango.search.smart.features.debug.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class DebugNode {

    public enum NodeTopology {ROOT, INTERIOR, TERMINAL, HORIZON, LOOP, QUIESCENCE, CHECK_EXTENSION, LEAF}

    /**
     * PV-Node Knuth's Type 1
     * Cut-Node Knuth's Type 2, also called fail-high node
     * All-Node Knuth's Type 3, also called fail-low node
     */
    public enum NodeType {PV, CUT, ALL}

    private NodeTopology topology;

    private NodeType type;

    private String fen;

    private int ply;

    private long zobristHash;

    private DebugNode parent;

    private Move selectedMove;

    private String fnString;

    private int alpha;

    private int beta;

    private int value;

    private Integer standingPat;

    private List<DebugOperationTT> entryRead = new ArrayList<>();

    private List<DebugOperationTT> entryWrite = new ArrayList<>();

    /**
     * Cual de los movimientos de este nodo es promovido como KillerMove
     */
    private Move killerMove;


    /**
     * Debug operaciones de ordenamiento
     */
    private int sortedPly; //Debiera ser igual a ply

    private List<String> sortedMoves;

    private List<DebugOperationTT> sorterReads = new ArrayList<>();

    private List<DebugOperationEval> evalCacheReads = new ArrayList<>();

    // Movimientos que estan presentes en KM table
    private List<Move> sorterKm = new ArrayList<>();


    private List<DebugNode> childNodes = new LinkedList<>();

    public void setDebugSearch(String fnString, int alpha, int beta) {
        this.fnString = fnString;
        this.alpha = alpha;
        this.beta = beta;
    }


    public void validate() {
        if (childNodes.stream().mapToLong(DebugNode::getZobristHash).distinct().count() != this.childNodes.size()) {
            throw new RuntimeException("Duplicated Node");
        }
    }
}
