package net.chesstango.search.gamegraph;

/**
 * @author Mauricio Coria
 */
interface NodeVisitor {
    void visit(Node node);

    void visit(NodeLink nodeLink);
}
