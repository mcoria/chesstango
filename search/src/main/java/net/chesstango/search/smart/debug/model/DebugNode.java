package net.chesstango.search.smart.debug.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Bound;

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

    private NodeTopology topology;

    /**
     * PV-Node Knuth's Type 1
     * Cut-Node Knuth's Type 2, also called fail-high node
     * All-Node Knuth's Type 3, also called fail-low node
     */
    public enum NodeType {PV, CUT, ALL}

    private NodeType type;

    private int depth;

    private int searchByWindowsCycle;

    private Bound bound;

    private String fen;

    private int ply;

    private long zobristHash;

    private DebugNode parent;

    private Move selectedMove;

    private String turn;

    private int alpha;

    private int beta;

    private int value;

    private Integer standingPat;

    private List<DebugReadTT> transpositionNodeReads = new ArrayList<>();
    private List<DebugWriteTT> transpositionNodeWrites = new ArrayList<>();

    private List<String> pv;

    /**
     * Cual de los movimientos de este nodo es promovido como KillerMove
     */
    private Move killerMove;

    private Move killerMovesTableA;

    private Move killerMovesTableB;

    /**
     * Debug operaciones de ordenamiento
     */
    private List<String> sortedMoves;

    private List<DebugReadTT> sorterHeadReads = new ArrayList<>();

    private List<DebugReadTT> sorterTailReads = new ArrayList<>();

    private List<DebugCacheRead> evalCacheReads = new ArrayList<>();

    private List<DebugPVReadTT> pvReads = new ArrayList<>();

    private List<DebugNode> childNodes = new LinkedList<>();

    public void setDebugSearch(String turn, int alpha, int beta) {
        this.turn = turn;
        this.alpha = alpha;
        this.beta = beta;
    }


    public void validate() {
        if (childNodes
                .stream()
                .mapToLong(DebugNode::getZobristHash)
                .distinct()
                .count() != this.childNodes.size()) {
            throw new RuntimeException("Duplicated Node");
        }
    }

    public void addChild(DebugNode newNode) {
        childNodes.add(newNode);
    }
}
