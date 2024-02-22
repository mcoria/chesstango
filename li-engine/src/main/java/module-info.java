module net.chesstango.li.engine {
    exports net.chesstango.li;

    requires chariot;
    requires org.slf4j;
    requires net.chesstango.engine;
    requires net.chesstango.search;
    requires net.chesstango.board;
    requires net.chesstango.uci;
    requires static lombok;
    requires java.management;
}