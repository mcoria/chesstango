module net.chesstango.uci.engine {
    exports net.chesstango.uci.engine;
    exports net.chesstango.uci.service;
    exports net.chesstango.uci.proxy;
    exports net.chesstango.uci.gui;
    exports net.chesstango.uci;
    requires net.chesstango.search;
    requires net.chesstango.board;
    requires net.chesstango.uci;
    requires net.chesstango.evaluation;
    requires net.chesstango.engine;
    requires com.fasterxml.jackson.databind;
    requires lombok;
}