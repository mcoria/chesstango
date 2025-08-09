module net.chesstango.engine {
    exports net.chesstango.engine;

    requires net.chesstango.board;
    requires net.chesstango.evaluation;
    requires net.chesstango.search;

    requires net.chesstango.gardel;
    requires net.chesstango.piazzolla;

    requires org.slf4j;

    requires static lombok;
}