package net.chesstango.engine.polyglot;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.polyglot.PolyglotEncoder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MappedPolyglotBookTest {

    @Test
    @Disabled
    public void testReadPerfect_2021() throws IOException {
        //PolyglotFileReader reader = new PolyglotFileReader("C:\\Java\\projects\\chess\\chess-utils\\books\\openings\\Perfect_2021\\BIN\\Perfect2021.bin");
        MappedPolyglotBook book = new MappedPolyglotBook();

        book.load(Path.of("C:\\Java\\projects\\chess\\chess-utils\\books\\openings\\polyglot-collection\\final-book.bin"));

        Long polyglotKey = PolyglotEncoder.getKey(FENDecoder.INITIAL_FEN);
        //Long polyglotKey = PolyglotEncoder.getKey("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        //Long polyglotKey = PolyglotEncoder.getKey("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1");
        //Long polyglotKey = PolyglotEncoder.getKey("rnbqkbnr/ppp1pppp/8/3p4/8/5N2/PPPPPPPP/RNBQKB1R w KQkq d6 0 2");
        //Long polyglotKey = PolyglotEncoder.getKey("rnbqkbnr/ppp1pppp/8/3p4/8/5NP1/PPPPPP1P/RNBQKB1R b KQkq - 0 2");
        //Long polyglotKey = PolyglotEncoder.getKey("rnbqkbnr/pp2pppp/2p5/3p4/8/5NP1/PPPPPP1P/RNBQKB1R w KQkq - 0 3");
        //Long polyglotKey = PolyglotEncoder.getKey("rnbqkbnr/pp2pppp/2p5/3p4/8/5NP1/PPPPPPBP/RNBQK2R b KQkq - 1 3");
        //Long polyglotKey = PolyglotEncoder.getKey("rn1qkbnr/pp2pppp/2p5/3p4/6b1/5NP1/PPPPPPBP/RNBQK2R w KQkq - 2 4");
        //Long polyglotKey = PolyglotEncoder.getKey("rn1qkbnr/pp2pppp/2p5/3p4/6b1/5NP1/PPPPPPBP/RNBQ1RK1 b kq - 3 4");
        //Long polyglotKey = PolyglotEncoder.getKey("r2qkbnr/pp1npppp/2p5/3p4/6b1/5NP1/PPPPPPBP/RNBQ1RK1 w kq - 4 5");
        //Long polyglotKey = PolyglotEncoder.getKey("r2qkbnr/pp1npppp/2p5/3p4/6b1/3P1NP1/PPP1PPBP/RNBQ1RK1 b kq - 0 5");
        //Long polyglotKey = PolyglotEncoder.getKey("r2qk1nr/pp1bppbp/2np2p1/1B6/4P3/2NQ1N2/PPP2PPP/R1B2RK1 b kq - 3 8");

        List<PolyglotEntry> possibleMoves = book.search(polyglotKey);

        if (possibleMoves != null) {
            for (PolyglotEntry move : possibleMoves) {
                System.out.println(move);
            }
        }

        book.close();
    }
}
