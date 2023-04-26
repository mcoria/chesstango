module net.chesstango.uci.engine {
    exports net.chesstango.uci.engine;
    exports net.chesstango.uci.service;
    exports net.chesstango.uci.proxy;
    exports net.chesstango.uci.gui;
    exports net.chesstango.engine;
    exports net.chesstango.uci.engine.builders;
    requires net.chesstango.search;
    requires net.chesstango.board;
    requires net.chesstango.uci;
    requires com.fasterxml.jackson.databind;
    requires net.chesstango.evaluation;
}