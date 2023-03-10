package net.chesstango.search.gamegraph;

public interface NodeVisitor {
    void visit(Node node);

    void visit(NodeLink nodeLink);
}
