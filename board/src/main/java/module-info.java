module net.chesstango.board {
    exports net.chesstango.board;
    exports net.chesstango.board.representations;
    exports net.chesstango.board.representations.polyglot;
    exports net.chesstango.board.representations.pgn;
    exports net.chesstango.board.representations.move;
    exports net.chesstango.board.representations.fen;
    exports net.chesstango.board.representations.epd;
    exports net.chesstango.board.representations.ascii;
    exports net.chesstango.board.position;
    exports net.chesstango.board.perft;
    exports net.chesstango.board.perft.imp;
    exports net.chesstango.board.moves;
    exports net.chesstango.board.moves.generators.pseudo;
    exports net.chesstango.board.moves.generators.legal;
    exports net.chesstango.board.moves.factories;
    exports net.chesstango.board.moves.containers;
    exports net.chesstango.board.iterators;
    exports net.chesstango.board.iterators.bysquare;
    exports net.chesstango.board.iterators.bysquare.bypiece;
    exports net.chesstango.board.factory;
    exports net.chesstango.board.builders;
    exports net.chesstango.board.analyzer;

    requires static lombok;
}