package net.chesstango.engine.manager;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.piazzolla.polyglot.MappedPolyglotBook;
import net.chesstango.piazzolla.polyglot.PolyglotEntry;
import net.chesstango.search.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static net.chesstango.search.SearchParameter.POLYGLOT_PATH;

/**
 * @author Mauricio Coria
 */
public final class SearchManagerByBook implements SearchManagerChain {

    private final MappedPolyglotBook book;

    @Setter
    private SearchManagerChain next;

    public SearchManagerByBook() {
        this.book = new MappedPolyglotBook();
    }

    @Override
    public void reset() {
        next.reset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        if (POLYGLOT_PATH.equals(parameter) && value instanceof String path) {
            book.load(Path.of(path));
        }
        next.setSearchParameter(parameter, value);
    }

    @Override
    public void setProgressListener(ProgressListener progressListener) {
        next.setProgressListener(progressListener);
    }

    @Override
    public void stopSearching() {
        next.stopSearching();
    }

    @Override
    public void open() {
        next.open();
    }

    @Override
    public void close() {
        try {
            book.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        next.close();
    }

    @Override
    public SearchResult search(Game game) {
        SearchResult searchResult = null;
        if (book.isLoaded()) {
            searchResult = searchByBook(game);
        }
        return searchResult == null ? next.search(game) : searchResult;
    }

    private SearchResult searchByBook(Game game) {
        List<PolyglotEntry> bookSearchResult = book.search(game.getPosition().getZobristHash());
        if (bookSearchResult != null) {
            MoveContainerReader<Move> possibleMoves = game.getPossibleMoves();
            for (PolyglotEntry polyglotEntry : bookSearchResult) {
                Square from = Square.getSquare(polyglotEntry.from_file(), polyglotEntry.from_rank());
                Square to = Square.getSquare(polyglotEntry.to_file(), polyglotEntry.to_rank());
                Move move = possibleMoves.getMove(from, to);
                if (move != null) {
                    MoveEvaluation bestMove = new MoveEvaluation(move, polyglotEntry.weight(), MoveEvaluationType.EXACT);
                    return new SearchResult()
                            .addSearchResultByDepth(new SearchResultByDepth(1).setBestMoveEvaluation(bestMove));
                }
            }
        }
        return null;
    }
}
