package net.chesstango.engine;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.piazzolla.syzygy.SyzygyPosition;
import net.chesstango.piazzolla.syzygy.SyzygyPositionBuilder;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.nio.file.Files;
import java.nio.file.Path;

import static net.chesstango.piazzolla.syzygy.Syzygy.TB_MAX_MOVES;
import static net.chesstango.piazzolla.syzygy.Syzygy.TB_RESULT_FAILED;


/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchByTablebase implements SearchChain {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

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
        if (tbLargest >= 3 && tbLargest >= Long.bitCount(game.getPosition().getAllPositions())) {
            SyzygyPositionBuilder positionBuilder = new SyzygyPositionBuilder();
            game.getPosition().export(positionBuilder);
            SyzygyPosition syzygyPosition = positionBuilder.getPositionRepresentation();

            int[] results = new int[TB_MAX_MOVES];

            int res = syzygy.tb_probe_root(syzygyPosition, results);

            if (res != TB_RESULT_FAILED) {
                final int fromIdx = Syzygy.TB_GET_FROM(res);
                final int toIdx = Syzygy.TB_GET_TO(res);

                Square from = Square.squareByIdx(fromIdx);
                Square to = Square.squareByIdx(toIdx);
                Move move = game.getMove(from, to);
                if (move != null) {
                    log.debug("Move found: {}", simpleMoveEncoder.encode(move));
                    MoveEvaluation bestMove = new MoveEvaluation(move, Syzygy.TB_GET_WDL(res), MoveEvaluationType.EXACT);
                    return new SearchResult()
                            .addSearchResultByDepth(new SearchResultByDepth(1).setBestMoveEvaluation(bestMove));
                }
            }
        }

        return null;
    }
}
