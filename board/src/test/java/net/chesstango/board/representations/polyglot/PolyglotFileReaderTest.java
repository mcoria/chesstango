package net.chesstango.board.representations.polyglot;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class PolyglotFileReaderTest {

    @Test
    @Ignore
    public void testRead(){
        PolyglotFileReader reader = new PolyglotFileReader();

        Map<Long, List<PolyglotFileReader.MoveBookEntry>> book = reader.read();

        //Long polyglotKey = getKey(FENDecoder.INITIAL_FEN);
        Long polyglotKey = PolyglotEncoder.getKey("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");

        List<PolyglotFileReader.MoveBookEntry> posibleMoves = book.get(polyglotKey);

        //System.out.println(posibleMoves);

        for (PolyglotFileReader.MoveBookEntry move: posibleMoves) {
            System.out.println(move);
        }
    }


}
