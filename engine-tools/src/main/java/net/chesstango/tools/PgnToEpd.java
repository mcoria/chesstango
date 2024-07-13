package net.chesstango.tools;

import net.chesstango.board.representations.pgn.PGNStringDecoder;

/**
 * @author Mauricio Coria
 */
public class PgnToEpd {

    public static void main(String[] args) {
        PGNStringDecoder pgnStringDecoder = new PGNStringDecoder();
        pgnStringDecoder.decodePGNs(System.in)
                .forEach(pgn -> pgn.toEPD()
                        .forEach(System.out::println)
                );
    }
}
