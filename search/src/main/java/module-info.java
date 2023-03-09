module net.chesstango.search {
    exports net.chesstango.search;
    exports net.chesstango.search.smart.minmax;
    exports net.chesstango.search.smart.negamax;
    exports net.chesstango.search.dummy;
    requires net.chesstango.evaluation;
    requires net.chesstango.board;
}