package net.chesstango.engine;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.polyglot.PolyglotEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;


/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchByOpenBook implements SearchByChain {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    @Setter
    private SearchByChain next;

    private final PolyglotBook book;

    private SearchByOpenBook(PolyglotBook book) {
        this.book = book;
    }

    static SearchByOpenBook open(String polyglotFile) {
        if (polyglotFile != null) {
            try {
                Path polyglotFilePath = Path.of(polyglotFile);
                if (Files.exists(polyglotFilePath)) {
                    return new SearchByOpenBook(PolyglotBook.open(polyglotFilePath));
                } else {
                    log.warn("Book file '{}' not found", polyglotFile);
                }
            } catch (IOException e) {
                log.error("Error opening book file", e);
            }
        }
        return null;
    }

    @Override
    public void reset() {
        next.reset();
    }


    @Override
    public void stopSearching() {
        next.stopSearching();
    }


    @Override
    public void close() throws Exception {
        if (book != null) {
            try {
                book.close();
            } catch (IOException e) {
                log.error("Error closing opening book", e);
            }
        }
        next.close();
    }

    @Override
    public SearchResponse search(SearchContext context) {
        SearchResponse searchResponse = null;
        if (book != null) {
            Optional<PolyglotEntry>  polyglotEntryOpt = searchByBook(context.getGame());
            if (polyglotEntryOpt.isPresent()) {
                searchResponse = createSearchResponse(context.getGame(), polyglotEntryOpt.get());
            }
        }
        return searchResponse == null ? next.search(context) : searchResponse;
    }

    private SearchResponse createSearchResponse(Game game, PolyglotEntry polyglotEntry) {
        SearchResponse searchResponse = null;

        Square from = Square.of(polyglotEntry.from_file(), polyglotEntry.from_rank());
        Square to = Square.of(polyglotEntry.to_file(), polyglotEntry.to_rank());

        MoveContainerReader<Move> possibleMoves = game.getPossibleMoves();
        Move move = possibleMoves.getMove(from, to);
        if (move != null) {
            log.debug("Move found: {}", simpleMoveEncoder.encode(move));
            searchResponse = new SearchByOpenBookResult(move, polyglotEntry);
        } else {
            log.warn("Move not found fromIdx={} toIdx={} fen={}", from, to, game.getCurrentFEN());
        }
        return searchResponse;
    }

    private Optional<PolyglotEntry> searchByBook(Game game) {
        List<PolyglotEntry> polyglotEntries = book.search(game.getPosition().getZobristHash());
        if (polyglotEntries != null && !polyglotEntries.isEmpty()) {
            return Optional.of(polyglotEntries.getFirst());
        }
        return Optional.empty();
    }
}
