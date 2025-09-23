module net.chesstango.uci.engine {
    exports net.chesstango.uci.engine;

    requires net.chesstango.search;
    requires net.chesstango.board;
    requires net.chesstango.engine;
    requires net.chesstango.goyeneche;
    requires net.chesstango.gardel;

    requires org.slf4j;
    requires static lombok;
    requires java.logging;
}