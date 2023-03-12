module net.chesstango.search {
    exports net.chesstango.search;
    exports net.chesstango.search.gamegraph;
    exports net.chesstango.search.smart.minmax;
    exports net.chesstango.search.dummy;
    exports net.chesstango.search.smart;
    requires net.chesstango.evaluation;
    requires net.chesstango.board;
    requires com.fasterxml.jackson.databind;

    opens net.chesstango.search.gamegraph;
    exports net.chesstango.search.smart.alphabeta;
}