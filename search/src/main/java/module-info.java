module net.chesstango.search {
    exports net.chesstango.search;
    exports net.chesstango.search.gamegraph;
    exports net.chesstango.search.smart.minmax;
    exports net.chesstango.search.smart.negamax;
    exports net.chesstango.search.smart.alphabeta;
    exports net.chesstango.search.dummy;
    exports net.chesstango.search.smart;
    exports net.chesstango.search.builders;

    requires net.chesstango.evaluation;
    requires net.chesstango.board;
    requires com.fasterxml.jackson.databind;

    opens net.chesstango.search.gamegraph;
}