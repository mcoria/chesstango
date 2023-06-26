package net.chesstango.search.polyglot;

import net.chesstango.board.representations.polyglot.PolyglotEncoder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class SimplePolyglotBookTest {

    @Test
    @Disabled
    public void testReadPerfect_2021() {
        //PolyglotFileReader reader = new PolyglotFileReader("C:\\Java\\projects\\chess\\chess-utils\\books\\openings\\Perfect_2021\\BIN\\Perfect2021.bin");
        SimplePolyglotBook book = new SimplePolyglotBook();

        book.load(Path.of("C:\\Java\\projects\\chess\\chess-utils\\books\\openings\\polyglot-collection\\final-book.bin"));

        //Long polyglotKey = FENDecoder.loadChessPosition(FENDecoder.INITIAL_FEN).getZobristHash();
        //Long polyglotKey = PolyglotEncoder.getKey("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        Long polyglotKey = PolyglotEncoder.getKey("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1");

        List<PolyglotEntry> possibleMoves = book.search(polyglotKey);

        for (PolyglotEntry move : possibleMoves) {
            System.out.println(move);
        }
    }
}
