package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluationType;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
@Getter
public class DebugNode {
    SearchNodeType nodeType;

    long zobristHash;

    DebugNode parent;

    Move selectedMove;

    String fnString;

    int alpha;

    int beta;

    int value;

    Integer standingPat;

    MoveEvaluationType moveEvaluationType;

    List<DebugNode> childNodes = new LinkedList<>();

    List<DebugNodeTT> transpositionOperations = new LinkedList<>();

    public void addChild(DebugNode newNode) {
        childNodes.add(newNode);
        newNode.parent = this;
    }

    public enum SearchNodeType {ROOT, INTERIOR, TERMINAL, HORIZON, QUIESCENCE, CHECK_EXTENSION, LEAF}
}
