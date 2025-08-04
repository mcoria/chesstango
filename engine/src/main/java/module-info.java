module net.chesstango.engine {
    exports net.chesstango.engine;


    requires net.chesstango.board;
    requires net.chesstango.search;

    requires net.chesstango.gardel;
    requires net.chesstango.piazzolla;

    requires static lombok;
    requires net.chesstango.evaluation;
}