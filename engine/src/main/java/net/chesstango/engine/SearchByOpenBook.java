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
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchByOpenBook implements SearchChain {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    @Setter
    private SearchChain next;

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
    public SearchResult search(SearchContext context) {
        SearchResult searchResult = null;
        if (book != null) {
            searchResult = searchByBook(context.getGame());
        }
        return searchResult == null ? next.search(context) : searchResult;
    }

    private SearchResult searchByBook(Game game) {
        List<PolyglotEntry> polyglotEntries = book.search(game.getPosition().getZobristHash());
        if (polyglotEntries != null && !polyglotEntries.isEmpty()) {
            MoveContainerReader<Move> possibleMoves = game.getPossibleMoves();
            PolyglotEntry polyglotEntry = polyglotEntries.getFirst();
            Square from = Square.of(polyglotEntry.from_file(), polyglotEntry.from_rank());
            Square to = Square.of(polyglotEntry.to_file(), polyglotEntry.to_rank());
            Move move = possibleMoves.getMove(from, to);
            if (move != null) {
                log.debug("Move found: {}", simpleMoveEncoder.encode(move));
                MoveEvaluation bestMove = new MoveEvaluation(move, polyglotEntry.weight(), MoveEvaluationType.EXACT);
                return new SearchResult()
                        .addSearchResultByDepth(new SearchResultByDepth(1).setBestMoveEvaluation(bestMove));
            }
        }
        return null;
    }
}
