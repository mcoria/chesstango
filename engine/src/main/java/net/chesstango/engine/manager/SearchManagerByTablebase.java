package net.chesstango.engine.manager;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.search.*;

import java.nio.file.Files;
import java.nio.file.Path;

import static net.chesstango.search.SearchParameter.SYZYGY_DIRECTORY;

/**
 * @author Mauricio Coria
 */
public final class SearchManagerByTablebase implements SearchManagerChain {
    @Setter
    private SearchManagerChain next;

    private Syzygy syzygy;

    private Path syzygyDirectory;

    @Override
    public void reset() {
        next.reset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        if (SYZYGY_DIRECTORY.equals(parameter) && value instanceof String path) {
            Path syzygyDirectory = Path.of(path);
            if (Files.isDirectory(syzygyDirectory)) {
                this.syzygyDirectory = syzygyDirectory;
            } else {
                System.err.println("Syzygy directory " + path + " not found");
            }
        }
        next.setSearchParameter(parameter, value);
    }

    @Override
    public void setSearchResultByDepthListener(SearchResultByDepthListener searchResultByDepthListener) {
        next.setSearchResultByDepthListener(searchResultByDepthListener);
    }

    @Override
    public void stopSearching() {
        next.stopSearching();
    }

    @Override
    public void open() {
        if (syzygyDirectory != null) {
            syzygy = new Syzygy();
            syzygy.setSyzygyDirectory(syzygyDirectory);
        }

        next.open();
    }

    @Override
    public void close() {
        if (syzygy != null) {
            /*
            try {
                //syzygy.close();
                System.err.println("Error closing opening book");
            } catch (IOException e) {
                System.err.println("Error closing opening book");
                e.printStackTrace(System.err);
            }
             */
            System.err.println("syzygy.close() not implemented");
        }
        next.close();
    }

    @Override
    public SearchResult search(Game game) {
        SearchResult searchResult = null;
        if (syzygy != null) {
            searchResult = searchByBook(game);
        }
        return searchResult == null ? next.search(game) : searchResult;
    }

    private SearchResult searchByBook(Game game) {
        return null;
    }
}
