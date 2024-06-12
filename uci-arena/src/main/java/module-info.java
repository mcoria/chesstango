module net.chesstango.uci.arena {
    exports net.chesstango.uci.arena;
    exports net.chesstango.uci.arena.gui;
    exports net.chesstango.uci.arena.listeners;
    exports net.chesstango.uci.arena.matchtypes;

    requires net.chesstango.uci;
    requires net.chesstango.uci.engine;
    requires net.chesstango.evaluation;
    requires net.chesstango.board;
    requires net.chesstango.search;
    requires net.chesstango.mbeans;
    requires net.chesstango.engine;

    requires org.apache.commons.pool2;
    requires java.management;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;

    requires static lombok;
}