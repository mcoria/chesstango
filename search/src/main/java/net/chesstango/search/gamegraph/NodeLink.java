package net.chesstango.search.gamegraph;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.chesstango.board.moves.Move;

public class NodeLink {

    @JsonBackReference
    Node parent;

    @JsonProperty("move")
    String moveStr;

    Move move;

    @JsonProperty("node")
    Node mockNode;


    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
        mockNode.accept(visitor);
    }

    public Move getMove() {
        return move;
    }
}
