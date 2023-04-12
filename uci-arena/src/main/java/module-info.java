module net.chesstango.uci.arena {
    exports net.chesstango.uci.arena;
    exports net.chesstango.uci.arena.listeners;

    requires net.chesstango.uci;
    requires net.chesstango.uci.engine;
    requires net.chesstango.evaluation;
    requires net.chesstango.board;
    requires net.chesstango.search;
    requires org.apache.commons.pool2;
    requires java.management;
    requires net.chesstango.mbeans;
}