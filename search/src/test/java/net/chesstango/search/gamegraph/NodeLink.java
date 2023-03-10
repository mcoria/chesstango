package net.chesstango.search.gamegraph;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeLink {

    @JsonBackReference
    Node parent;

    @JsonProperty("move")
    String move;

    @JsonProperty("node")
    Node mockNode;


    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
        mockNode.accept(visitor);
    }
}
