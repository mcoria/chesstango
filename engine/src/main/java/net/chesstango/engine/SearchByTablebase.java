package net.chesstango.engine;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.search.SearchResult;

import java.nio.file.Files;
import java.nio.file.Path;


/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchByTablebase implements SearchChain {

    @Setter
    private SearchChain next;

    private final Syzygy syzygy;

    private SearchByTablebase(Syzygy syzygy) {
        this.syzygy = syzygy;
    }

    static SearchByTablebase open(String syzygyDirectory) {
        if (syzygyDirectory != null) {
            Path syzygyDirectoryPath = Path.of(syzygyDirectory);
            if (Files.isDirectory(syzygyDirectoryPath)) {
                return new SearchByTablebase(Syzygy.open(syzygyDirectoryPath));
            } else {
                log.error("Syzygy directory '{}' not found", syzygyDirectory);
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
        /*
        try {
            syzygy.close();
            System.err.println("Error closing opening book");
        } catch (IOException e) {
            System.err.println("Error closing opening book");
            e.printStackTrace(System.err);
        }
         */
        next.close();
    }

    @Override
    public SearchResult search(SearchContext context) {
        SearchResult searchResult = null;
        if (syzygy != null) {
            searchResult = searchByBook(context.getGame());
        }
        return searchResult == null ? next.search(context) : searchResult;
    }

    SearchResult searchByBook(Game game) {
        final int tbLargest = syzygy.tb_largest();
        if (tbLargest >= 3 && tbLargest >= Long.bitCount(game.getPosition().getAllPositions()) ) {
            //game.
        }
        return null;
    }
}
