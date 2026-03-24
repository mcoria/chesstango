module net.chesstango.reports {
    exports net.chesstango.reports;
    exports net.chesstango.reports.engine;
    exports net.chesstango.reports.search.nodes.depth;
    exports net.chesstango.reports.search.nodes.types;
    exports net.chesstango.reports.search.pv;
    exports net.chesstango.reports.search.transposition;
    exports net.chesstango.reports.search;
    exports net.chesstango.reports.search.board;
    exports net.chesstango.reports.search.iteration;
    exports net.chesstango.reports.search.evaluation;


    requires net.chesstango.search;
    requires net.chesstango.board;
    requires net.chesstango.evaluation;
    requires net.chesstango.gardel;
    requires net.chesstango.engine;
    requires net.chesstango.piazzolla;
    requires jmh.core;

    requires static lombok;
}