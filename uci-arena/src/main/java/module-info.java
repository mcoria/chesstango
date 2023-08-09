module net.chesstango.uci.arena {
    exports net.chesstango.uci.arena;
    exports net.chesstango.uci.arena.listeners;

    requires net.chesstango.uci;
    requires net.chesstango.uci.engine;
    requires net.chesstango.evaluation;
    requires net.chesstango.board;
    requires net.chesstango.search;
    requires net.chesstango.mbeans;
    requires net.chesstango.engine;
    requires org.apache.commons.pool2;
    requires java.management;
}