module net.chesstango.engine {
    exports net.chesstango.engine;
    exports net.chesstango.engine.polyglot;

    requires net.chesstango.board;
    requires net.chesstango.search;
    requires net.chesstango.evaluation;
    requires static lombok;
}