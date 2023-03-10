package net.chesstango.search.gamegraph;

interface NodeVisitor {
    void visit(Node node);

    void visit(NodeLink nodeLink);
}
