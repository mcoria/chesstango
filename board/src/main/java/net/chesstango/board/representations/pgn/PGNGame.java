package net.chesstango.board.representations.pgn;

import java.util.List;

public class PGNGame {
    private final PGNHeader header;
    private final List<String> moveList;

    public PGNGame(PGNHeader header, List<String> moveList) {
        this.header = header;
        this.moveList = moveList;
    }

    public PGNHeader getHeader() {
        return header;
    }

    public List<String> getMoveList() {
        return moveList;
    }

}
