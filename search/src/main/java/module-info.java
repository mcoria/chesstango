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
    exports net.chesstango.search.smart.alphabeta.listeners;
    exports net.chesstango.search.smart.sorters;
    exports net.chesstango.search.smart.statistics;
    exports net.chesstango.search.smart.transposition;

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
}