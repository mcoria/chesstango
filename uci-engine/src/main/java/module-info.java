module net.chesstango.uci.engine {
    exports net.chesstango.uci.engine;
    exports net.chesstango.uci.service;
    exports net.chesstango.uci;
    exports net.chesstango.uci.proxy;
    requires net.chesstango.search;
    requires net.chesstango.board;
    requires net.chesstango.uci;
    requires net.chesstango.evaluation;
    requires net.chesstango.engine;
    requires org.slf4j;
    requires static lombok;
}