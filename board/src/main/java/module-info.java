module net.chesstango.board {
    exports net.chesstango.board;
    exports net.chesstango.board.representations;
    exports net.chesstango.board.representations.pgn;
    exports net.chesstango.board.representations.move;
    exports net.chesstango.board.position;
    exports net.chesstango.board.moves;
    exports net.chesstango.board.moves.generators.pseudo;
    exports net.chesstango.board.moves.generators.legal;
    exports net.chesstango.board.moves.containers;
    exports net.chesstango.board.iterators;
    exports net.chesstango.board.iterators.bysquare;
    exports net.chesstango.board.iterators.byposition;
    exports net.chesstango.board.builders;
    exports net.chesstango.board.analyzer;

    requires net.chesstango.gardel;

    requires static lombok;
}