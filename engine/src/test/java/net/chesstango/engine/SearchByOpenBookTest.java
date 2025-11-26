package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.polyglot.PolyglotEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class SearchByOpenBookTest {
    private SearchByOpenBook searchByOpenBook;

    @Mock
    private PolyglotBook book;

    @BeforeEach
    public void setup() {
        searchByOpenBook = new SearchByOpenBook(book);
    }

    @Test
    public void testSearch_CastlingWhiteKing() {
        when(book.search(0xBB2451EF62DACE33L)).thenReturn(List.of(new PolyglotEntry(0xBB2451EF62DACE33L, 4, 0, 7, 0, 130)));

        Game game = Game.from(FEN.of("r2qkbnr/pp1n1pp1/2p1p2p/3pPb2/3P4/2P2N2/PP2BPPP/RNBQK2R w KQkq - 0 7"));

        SearchContext context = new SearchContext()
                .setGame(game);

        SearchResponse response = searchByOpenBook.search(context);

        assertNotNull(response);

        assertInstanceOf(SearchByOpenBookResult.class, response);

        // Caslting
        Move move = response.move();

        assertEquals(Square.e1, move.getFrom().square());

        assertEquals(Square.g1, move.getTo().square());
    }


    @Test
    public void testPolyglotEntry_CastlingWhiteKing() {
        Game game = Game.from(FEN.of("r2qkbnr/pp1n1pp1/2p1p2p/3pPb2/3P4/2P2N2/PP2BPPP/RNBQK2R w KQkq - 0 7"));

        Optional<Move> optMove = SearchByOpenBook.polyglotEntryToMove(game, new PolyglotEntry(0xBB2451EF62DACE33L, 4, 0, 7, 0, 130));

        assertTrue(optMove.isPresent());

        // Caslting
        Move move = optMove.get();

        assertEquals(Square.e1, move.getFrom().square());

        assertEquals(Square.g1, move.getTo().square());
    }

    @Test
    public void testPolyglotEntry_CastlingWhiteQueen() {
        Game game = Game.from(FEN.of("r6r/pN1nkpp1/2p1pn1p/7P/3q4/1Q6/PPPN1PP1/R3K2R w KQ - 0 18"));

        Optional<Move> optMove = SearchByOpenBook.polyglotEntryToMove(game, new PolyglotEntry(0xBB2451EF62DACE33L, 4, 0, 0, 0, 130));

        assertTrue(optMove.isPresent());

        // Caslting
        Move move = optMove.get();

        assertEquals(Square.e1, move.getFrom().square());

        assertEquals(Square.c1, move.getTo().square());
    }


    @Test
    public void testPolyglotEntry_CastlingBlackKing() {
        Game game = Game.from(FEN.of("r1bqk2r/5ppp/p1np1b2/1p1Np3/4P3/N1P5/PP3PPP/R2QKB1R b KQkq - 0 12"));

        Optional<Move> optMove = SearchByOpenBook.polyglotEntryToMove(game, new PolyglotEntry(0xBB2451EF62DACE33L, 4, 7, 7, 7, 130));

        assertTrue(optMove.isPresent());

        // Caslting
        Move move = optMove.get();

        assertEquals(Square.e8, move.getFrom().square());

        assertEquals(Square.g8, move.getTo().square());
    }

    @Test
    public void testPolyglotEntry_CastlingBlackQueen() {
        Game game = Game.from(FEN.of("r3k2r/4bp2/pqbppp2/1p3P1p/4P2Q/2N5/PPP3PP/1K1R1B1R b kq - 3 16"));

        Optional<Move> optMove = SearchByOpenBook.polyglotEntryToMove(game, new PolyglotEntry(0xBB2451EF62DACE33L, 4, 7, 0, 7, 130));

        assertTrue(optMove.isPresent());

        // Caslting
        Move move = optMove.get();

        assertEquals(Square.e8, move.getFrom().square());

        assertEquals(Square.c8, move.getTo().square());
    }

}
