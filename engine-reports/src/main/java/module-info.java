module net.chesstango.reports {
    exports net.chesstango.reports.detail.evaluation;
    exports net.chesstango.reports.detail.nodes;
    exports net.chesstango.reports.detail.pv;
    exports net.chesstango.reports.summary;
    exports net.chesstango.reports.detail;

    requires net.chesstango.search;
    requires net.chesstango.board;
    requires net.chesstango.evaluation;

    requires static lombok;
}