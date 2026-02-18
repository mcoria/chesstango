module net.chesstango.reports {
    exports net.chesstango.reports;
    exports net.chesstango.reports.engine;
    exports net.chesstango.reports.search.evaluation;
    exports net.chesstango.reports.search.nodes;
    exports net.chesstango.reports.search.pv;
    exports net.chesstango.reports.search.transposition;
    exports net.chesstango.reports.search;

    requires net.chesstango.search;
    requires net.chesstango.board;
    requires net.chesstango.evaluation;
    requires net.chesstango.gardel;
    requires net.chesstango.engine;

    requires static lombok;
}