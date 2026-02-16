module net.chesstango.reports {
    exports net.chesstango.reports;
    exports net.chesstango.reports.tree.evaluation;
    exports net.chesstango.reports.tree.nodes;
    exports net.chesstango.reports.tree.pv;
    exports net.chesstango.reports.engine;
    exports net.chesstango.reports.tree.summary;

    requires net.chesstango.search;
    requires net.chesstango.board;
    requires net.chesstango.evaluation;
    requires net.chesstango.gardel;
    requires net.chesstango.engine;

    requires static lombok;
}