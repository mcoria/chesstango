module net.chesstango.reports {
    exports net.chesstango.reports.evaluation;
    exports net.chesstango.reports.nodes;
    exports net.chesstango.reports.pv;

    requires net.chesstango.search;
    requires net.chesstango.board;
    requires net.chesstango.evaluation;

    requires static lombok;
}