module net.chesstango.engine {
    exports net.chesstango.engine;
    exports net.chesstango.engine.builders;
    requires net.chesstango.board;
    requires net.chesstango.search;
    requires net.chesstango.uci;
    requires net.chesstango.evaluation;
    requires static lombok;
}