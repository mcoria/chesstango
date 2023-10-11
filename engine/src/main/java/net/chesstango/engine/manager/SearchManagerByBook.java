package net.chesstango.engine.manager;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.engine.polyglot.MappedPolyglotBook;
import net.chesstango.engine.polyglot.PolyglotEntry;
import net.chesstango.search.SearchMoveResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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
    public SearchMoveResult searchImp(Game game, int depth) {
        SearchMoveResult searchResult = null;
        if(book.isLoaded()) {
            searchResult = searchByBook(game);
        }
        return searchResult == null ? next.searchImp(game, depth) : searchResult;
    }

    public void setPolyglotBook(String path) {
        book.load(Path.of(path));
    }


    private SearchMoveResult searchByBook(Game game) {
        List<PolyglotEntry> bookSearchResult = book.search(game.getChessPosition().getZobristHash());
        if (bookSearchResult != null) {
            MoveContainerReader possibleMoves = game.getPossibleMoves();
            for (PolyglotEntry polyglotEntry : bookSearchResult) {
                Move move = possibleMoves.getMove(polyglotEntry.from(), polyglotEntry.to());
                if (move != null) {
                    return new SearchMoveResult(1, 0, move, null);
                }
            }
        }
        return null;
    }
}
