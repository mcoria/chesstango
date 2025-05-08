module net.chesstango.lichess.engine {
    exports net.chesstango.lichess;

    requires chariot;
    requires org.slf4j;
    requires net.chesstango.engine;
    requires net.chesstango.search;
    requires net.chesstango.board;

    requires java.management;
    requires net.chesstango.gardel;
}