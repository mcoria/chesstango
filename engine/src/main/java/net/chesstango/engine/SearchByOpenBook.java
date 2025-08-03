package net.chesstango.engine;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
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
class SearchByOpenBook implements SearchChain {
    @Setter
    private SearchChain next;

    private final PolyglotBook book;

    private SearchByOpenBook(PolyglotBook book) {
        this.book = book;
    }

    static SearchByOpenBook open(String polyglotFile) {
        try {
            Path polyglotFilePath = Path.of(polyglotFile);
            if (Files.exists(polyglotFilePath)) {
                return new SearchByOpenBook(PolyglotBook.open(polyglotFilePath));
            } else {
                System.err.println("Book file '" + polyglotFile + "' not found");
            }
        } catch (IOException e) {
            System.err.println("Error opening book '" + polyglotFile + "' not found");
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
                System.err.println("Error closing opening book");
                e.printStackTrace(System.err);
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
        List<PolyglotEntry> bookSearchResult = book.search(game.getPosition().getZobristHash());
        if (bookSearchResult != null) {
            MoveContainerReader<Move> possibleMoves = game.getPossibleMoves();
            for (PolyglotEntry polyglotEntry : bookSearchResult) {
                Square from = Square.of(polyglotEntry.from_file(), polyglotEntry.from_rank());
                Square to = Square.of(polyglotEntry.to_file(), polyglotEntry.to_rank());
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
