package net.chesstango.board.representations;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranscodingTest {

    @Test
    public void testTranscoding01(){
        Transcoding transcoding = new Transcoding();

        List<String> fenPositions = transcoding.pgnFileToFenPositions(this.getClass().getClassLoader().getResourceAsStream("main/pgn/Balsa_Top10.pgn"));

        List<String> expectedFenPositions = Arrays.asList(
                "r2qkbnr/pp1n1ppp/2p1p3/3pPb2/3P4/2P2N2/PP2BPPP/RNBQK2R b KQkq - 3 6",
                "r1bqkb1r/1p3ppp/p1nppn2/6B1/3NP3/2N5/PPPQ1PPP/2KR1B1R b kq - 1 8",
                "rnbqkb1r/5ppp/p2ppn2/1p6/3NP3/2N1BP2/PPP3PP/R2QKB1R w KQkq b6 0 8",
                "rn1qkb1r/1p3ppp/p2pbn2/4p3/4P3/1NN1BP2/PPP3PP/R2QKB1R b KQkq - 0 8",
                "r1bqkb1r/pp1n1ppp/2n1p3/2ppP3/3P1P2/2N1BN2/PPP3PP/R2QKB1R b KQkq - 3 7",
                "r1bq1rk1/ppp2ppp/2np1n2/2b1p3/2B1P3/2PP1N2/PP3PPP/RNBQ1RK1 w - - 2 7",
                "r1bq1rk1/2ppbppp/p1n2n2/1p2p3/4P3/1B3N2/PPPP1PPP/RNBQR1K1 w - - 2 8",
                "r1b1kb1r/pp1n1pp1/2p1pq1p/3p4/2PP4/2N1PN2/PP3PPP/R2QKB1R w KQkq - 1 8",
                "r1bqk2r/pp1n1ppp/2pbpn2/3p4/2PP4/2N1PN2/PPQ2PPP/R1B1KB1R w KQkq - 4 7",
                "rn1qk2r/p1pp1ppp/bp2pn2/8/1bPP4/1P3NP1/P2BPP1P/RN1QKB1R b KQkq - 2 6");

        for (int i = 0; i < 10; i++) {
            assertEquals(expectedFenPositions.get(i), fenPositions.get(i));
        }
    }

}
