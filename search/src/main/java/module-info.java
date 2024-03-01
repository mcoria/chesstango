module net.chesstango.search {
    exports net.chesstango.search;
    exports net.chesstango.search.dummy;
    exports net.chesstango.search.builders;
    exports net.chesstango.search.gamegraph;
    exports net.chesstango.search.reports;
    exports net.chesstango.search.smart;
    exports net.chesstango.search.smart.minmax;
    exports net.chesstango.search.smart.negamax;
    exports net.chesstango.search.smart.alphabeta;
    exports net.chesstango.search.smart.alphabeta.filters;
    exports net.chesstango.search.smart.alphabeta.filters.once;
    exports net.chesstango.search.smart.alphabeta.filters.transposition;
    exports net.chesstango.search.smart.alphabeta.listeners;
    exports net.chesstango.search.smart.sorters;
    exports net.chesstango.search.smart.statistics;
    exports net.chesstango.search.smart.transposition;
    exports net.chesstango.search.smart.alphabeta.debug;
    exports net.chesstango.search.smart.sorters.comparators;
    exports net.chesstango.search.reports.nodes;
    exports net.chesstango.search.reports.pv;
    exports net.chesstango.search.reports.evaluation;
    exports net.chesstango.search.reports.summary;

    requires net.chesstango.evaluation;
    requires net.chesstango.board;
    requires com.fasterxml.jackson.databind;

    requires static lombok;
    requires org.slf4j;

    opens net.chesstango.search.gamegraph;
    opens net.chesstango.search.reports;
    opens net.chesstango.search;
    opens net.chesstango.search.smart;
    opens net.chesstango.search.smart.statistics;
    opens net.chesstango.search.smart.alphabeta.debug;
    opens net.chesstango.search.reports.evaluation;
    opens net.chesstango.search.reports.nodes;
    opens net.chesstango.search.reports.pv;
    opens net.chesstango.search.reports.summary;
    exports net.chesstango.search.smart.alphabeta.debug.model;
    opens net.chesstango.search.smart.alphabeta.debug.model;
}